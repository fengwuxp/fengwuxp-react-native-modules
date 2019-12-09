//
//  PaymentModule.m
//  ReactNativeExample
//
//  Created by Benjamin on 2019/12/6.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import "PaymentModule.h"
#import <AlipaySDK/AlipaySDK.h>
@implementation PaymentModule


RCTPromiseResolveBlock resolveBlock = nil;
RCTPromiseRejectBlock rejectBlock = nil;

- (instancetype)init
{
  self = [super init];
  if (self) {
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(handleWXPay:) name:@"WXPay" object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(handleAliPay:) name:@"AliPay" object:nil];
  }
  return self;
}

- (void)dealloc {
  [[NSNotificationCenter defaultCenter] removeObserver:self];
}

/// 微信注册
/// @param APP_ID APP_ID
RCT_EXPORT_METHOD(registerApp:(NSString *)APP_ID) {
  [WXApi registerApp:APP_ID universalLink:@""];
}


RCT_REMAP_METHOD(isSupported, // 判断是否支持调用微信SDK
                 resolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject){
  if (![WXApi isWXAppInstalled]) resolve(@NO);
  else resolve(@YES);
}


/// 微信支付
/// @param reject 订单信息
RCT_EXPORT_METHOD(wxChatPay:(NSDictionary *)orderInfo
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
  resolveBlock = resolve;
  rejectBlock = reject;
  //调起微信支付
  //注意order取的值对应的key要和自己服务器提供的一致
  PayReq *req = [[PayReq alloc] init];
  req.partnerId = [orderInfo objectForKey:@"partnerId"];
  req.prepayId = [orderInfo objectForKey:@"prepayId"];
  req.nonceStr = [orderInfo objectForKey:@"nonceStr"];
  req.timeStamp = [[orderInfo objectForKey:@"timeStamp"] intValue];
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


RCT_EXPORT_METHOD(aliPay:(NSString *)orderInfo resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
  __weak typeof(self) wSelf = self;
  [[AlipaySDK defaultService] payOrder:orderInfo fromScheme:@"distributionylfkj" callback:^(NSDictionary *resultDic) {
      __strong typeof(wSelf) sSelf = wSelf;
      [sSelf dealWithAliPayResult:resultDic];
  }];
}

// 支付宝回调
- (void)handleAliPay:(NSNotification *)aNotification {
  NSDictionary *resultDic = [aNotification userInfo][@"result"];
  [self dealWithAliPayResult:resultDic];
}

// 支付宝处理
- (void)dealWithAliPayResult:(NSDictionary*)resultDic {
  NSString *msg;
  if ([resultDic[@"resultStatus"] intValue] == 6001) {
    msg = @"用户取消支付";
    rejectBlock(resultDic[@"resultStatus"], msg, nil);
  } else if ([resultDic[@"resultStatus"] intValue] == 6002) {
    msg = @"网络连接失败，请检查网络";
    rejectBlock(resultDic[@"resultStatus"], msg, nil);
  }else if ([resultDic[@"resultStatus"] intValue]==9000) {
    NSLog(@"支付宝支付成功");
    resolveBlock(nil);
  }else if ([resultDic[@"resultStatus"] intValue] == 4000) {
    msg = @"支付失败";
    rejectBlock(resultDic[@"resultStatus"], msg, nil);
  } else {
    rejectBlock(@"-1", @"支付错误", nil);
  }
}


RCT_EXPORT_MODULE(wxPay);




@end

