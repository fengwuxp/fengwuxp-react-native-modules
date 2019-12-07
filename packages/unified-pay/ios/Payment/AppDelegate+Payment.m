//
//  AppDelegate+Payment.m
//  ReactNativeExample
//
//  Created by Benjamin on 2019/12/6.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import "AppDelegate+Payment.h"


 
@implementation AppDelegate (Payment)

//支付回调9以后
- (BOOL)application:(UIApplication *)app openURL:(NSURL *)url options:(NSDictionary*)options {
 return  [WXApi handleOpenURL:url delegate:self];
}
//支付回调9以前
- (BOOL)application:(UIApplication *)application handleOpenURL:(NSURL *)url {
 return  [WXApi handleOpenURL:url delegate:self];
}
//ios9以后的方法
- (BOOL)application:(UIApplication *)application openURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication annotation:(id)annotation {
 return [WXApi handleOpenURL:url delegate:self];
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
