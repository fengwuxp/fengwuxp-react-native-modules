//
//  MobShareModule.m
//  ReactNativeExample
//
//  Created by Benjamin on 2019/12/9.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import "MobShareModule.h"
#import <ShareSDK/ShareSDK.h>
#import "ThirdConfigureModel.h"

@implementation MobShareModule

RCTPromiseResolveBlock resolveBlock = nil;
RCTPromiseRejectBlock rejectBlock = nil;
 
RCT_EXPORT_MODULE(MobShareModule)


RCT_EXPORT_METHOD(setup) {
  [ShareSDK registPlatforms:^(SSDKRegister *platformsRegister) {
    //（各社交平台申请AppKey的网址及申请流程汇总）
    //http://bbs.mob.com/forum.php?mod=viewthread&tid=275&page=1&extra=#pid860
    ThirdConfigureModel *configure = [ThirdConfigureModel new];
    //QQ
    [platformsRegister setupQQWithAppId:configure.QQId appkey:configure.QQKey];

    //微信
    [platformsRegister setupWeChatWithAppId:configure.WXId appSecret:configure.wxSecret universalLink:@""];

    //新浪
    [platformsRegister setupSinaWeiboWithAppkey:configure.SinaId appSecret:configure.SinaSecret redirectUrl:@""];
  }];
}


/// 授权
/// @param platform 平台码
/// @param resolve 成功回调
/// @param reject 失败回调
RCT_EXPORT_METHOD(authorize:(NSString *)platform resolver:(RCTPromiseResolveBlock)resolve
rejecter:(RCTPromiseRejectBlock)reject) {
  resolveBlock = resolve;
  rejectBlock = reject;
  
  [ShareSDK authorize:platform.integerValue settings:nil onStateChanged:^(SSDKResponseState state, SSDKUser *user, NSError *error) {

      switch (state) {
        case SSDKResponseStateSuccess: {
          NSLog(@"%@",[user.credential rawData]);
          resolveBlock(user);
        }
              break;
        case SSDKResponseStateFail: {
          NSLog(@"--%@",error.description);
          //失败
          rejectBlock(@"2", @"授权失败", error);
          break;
       }
        case SSDKResponseStateCancel: {
          //用户取消授权
          rejectBlock(@"3", @"用户取消授权", error);
        }
            break;

        default:
            break;
      }
  }];
}

/// 分享
/// @param platform 平台码
/// @param param 分享参数 title text url img
/// @param resolve 成功回调
/// @param reject 失败回调
RCT_EXPORT_METHOD(share:(NSString *)platform param:(NSDictionary *)param  resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
  resolveBlock = resolve;
  rejectBlock = reject;
  NSMutableDictionary *params = [NSMutableDictionary dictionary];
  [params SSDKSetupShareParamsByText:param[@"text"]
                              images:param[@"img"]
                                 url:param[@"url"]
                               title:param[@"title"]
                                type:SSDKContentTypeAuto];

  [ShareSDK share:platform.integerValue
       parameters:params
   onStateChanged:^(SSDKResponseState state, NSDictionary *userData,
  SSDKContentEntity *contentEntity, NSError *error) {
    switch (state) {
      case SSDKResponseStateSuccess: {
        //成功
        resolveBlock(nil);
      }
            break;
      case SSDKResponseStateFail: {
        //失败
        reject(@"2", @"分享失败", error);
        break;
      }
      case SSDKResponseStateCancel:
        //取消
        reject(@"3", @"用户取消分享", error);
        break;

      default:
          break;
      }
  }];
}

@end
