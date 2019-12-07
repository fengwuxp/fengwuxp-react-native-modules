//
//  PaymentModule.h
//  ReactNativeExample
//
//  Created by Benjamin on 2019/12/6.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import <React/RCTBridgeModule.h>
#import <React/RCTLog.h>
#import <WXApi.h>
#import <WXApiObject.h>

NS_ASSUME_NONNULL_BEGIN

@interface PaymentModule : NSObject <RCTBridgeModule, WXApiDelegate>
 
@end

NS_ASSUME_NONNULL_END
