//
//  PaymentModule.m
//  ReactNativeExample
//
//  Created by Benjamin on 2019/12/6.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import "PaymentModule.h"

@implementation PaymentModule


RCTPromiseResolveBlock resolveBlock = nil;
RCTPromiseRejectBlock rejectBlock = nil;

- (instancetype)init
{
  self = [super init];
  if (self) {
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(handleWXPay:) name:@"WXPay" object:nil];
  }
  return self;
}

- (void)dealloc {
  [[NSNotificationCenter defaultCenter] removeObserver:self];
}

RCT_EXPORT_METHOD(registerApp:(NSString *)APP_ID) {
  [WXApi registerApp:APP_ID universalLink:@""];
}

RCT_EXPORT_METHOD(wxChatPay:(NSDictionary *)orderInfo
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
  resolveBlock = resolve;
  rejectBlock = reject;
  //调起微信支付
  //注意order取的值对应的key要和自己服务器提供的一致
  PayReq *req = [[PayReq alloc] init];
  req.partnerId = [orderInfo objectForKey:@"partnerid"];
  req.prepayId = [orderInfo objectForKey:@"prepayid"];
  req.nonceStr = [orderInfo objectForKey:@"noncestr"];
  req.timeStamp = [[orderInfo objectForKey:@"timestamp"] intValue];
  req.package = [orderInfo objectForKey:@"package"];
  req.sign = [orderInfo objectForKey:@"sign"];
  [WXApi sendReq:req completion:^(BOOL success) {
    NSLog(@"wxpay %d", success);
  }];
}

/// 微信回调处理
/// @param aNotification 错误码
- (void)handleWXPay:(NSNotification *)aNotification {
  
  NSString * errCode =  [aNotification userInfo][@"errCode"];
  switch (errCode.integerValue) {
      case WXSuccess:
      {// 支付成功，向后台发送消息
        resolveBlock(nil);
      }
          break;
      case WXErrCodeCommon:
      { //签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等
        rejectBlock(@"-1", @"配置错误", nil);
      }
          break;
      case WXErrCodeUserCancel:
      { //用户点击取消并返回
        rejectBlock(@"-2", @"用户中途取消付款！", nil);
      }
          break;
      case WXErrCodeSentFail:
      { //发送失败
          rejectBlock(@"-3", @"微信发送失败", nil);
      }
          break;
      case WXErrCodeUnsupport:
      { //微信不支持
          rejectBlock(@"-4", @"微信不支持", nil);
      }
          break;
      case WXErrCodeAuthDeny:
      { //授权失败
        rejectBlock(@"-5", @"微信授权失败", nil);
      }
          break;
      default:
          break;
  }
}

RCT_REMAP_METHOD(isSupported, // 判断是否支持调用微信SDK
                 resolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject){
  if (![WXApi isWXAppInstalled]) resolve(@NO);
  else resolve(@YES);
}

RCT_EXPORT_MODULE(wxPay);



 
@end

