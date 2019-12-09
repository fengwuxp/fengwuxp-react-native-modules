//
//  AppDelegate+Payment.m
//  ReactNativeExample
//
//  Created by Benjamin on 2019/12/6.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import "AppDelegate+Payment.h"
#import <AlipaySDK/AlipaySDK.h>

 
@implementation AppDelegate (Payment)

//支付回调9以后
- (BOOL)application:(UIApplication *)app openURL:(NSURL *)url options:(NSDictionary*)options {
  if ([url.host isEqualToString:@"safepay"]) {
    //跳转支付宝钱包进行支付，处理支付结果
    [[AlipaySDK defaultService] processOrderWithPaymentResult:url standbyCallback:^(NSDictionary *resultDic) {
        NSLog(@"result = %@",resultDic);
    }];
  }else if ([url.host isEqualToString:@"pay"]) {
     [WXApi handleOpenURL:url delegate:self];
  }
    return YES;
}
//支付回调9以前
- (BOOL)application:(UIApplication *)application handleOpenURL:(NSURL *)url {
 //微信
 if ([url.host isEqualToString:@"pay"]) {
     [WXApi handleOpenURL:url delegate:self];
 }
 //支付宝
 else if ([url.host isEqualToString:@"safepay"]) {
         //跳转支付宝钱包进行支付，处理支付结果
     [[AlipaySDK defaultService] processOrderWithPaymentResult:url standbyCallback:^(NSDictionary *resultDic) {
         NSLog(@"result = %@",resultDic);
       NSNotification * notification = [NSNotification notificationWithName:@"AliPay" object:nil userInfo:@{@"result":resultDic}];
       [[NSNotificationCenter defaultCenter] postNotification:notification];
     }];
 }
 return YES;
}
 

//ios9以后的方法
- (BOOL)application:(UIApplication *)application openURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication annotation:(id)annotation {
  if ([url.host isEqualToString:@"safepay"]) {
    //跳转支付宝钱包进行支付，处理支付结果
    [[AlipaySDK defaultService] processOrderWithPaymentResult:url standbyCallback:^(NSDictionary *resultDic) {
        NSLog(@"result = %@",resultDic);
    }];
  } else if ([url.host isEqualToString:@"pay"]) {
      [WXApi handleOpenURL:url delegate:self];
  }
  return YES;
}
 

//微信结果回调的方法   收到微信的回应
-(void) onResp:(BaseResp*)resp {
    //支付类型
    if ([resp isKindOfClass:[PayResp class]]) {
        //支付返回结果，实际支付结果需要去微信服务器端查询
        NSNotification * notification = [NSNotification notificationWithName:@"WXPay" object:nil userInfo:@{@"errCode":@(resp.errCode)}];
        [[NSNotificationCenter defaultCenter] postNotification:notification];
        
    }
}
@end
