//
//  ThirdConfigureModel.m
//  vipbusgate
//
//  Created by BenjaminRichard on 2016/10/28.
//  Copyright © 2016年 BenjaminRichard. All rights reserved.
//

#import "ThirdConfigureModel.h"

@implementation ThirdConfigureModel

- (instancetype)init {
    
    @try {
        NSArray *infoArray = [[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleURLTypes"];
        NSDictionary *keyDic = [[[NSBundle mainBundle] infoDictionary] objectForKey:@"ThirdKey"];
        for (NSDictionary *dic in infoArray) {
            if ([@"qq"  isEqual: [dic objectForKey:@"CFBundleURLName"]]) {
                _QQId = [dic objectForKey:@"CFBundleURLSchemes"][0];
                _QQId = [_QQId substringFromIndex:2];
                _QQId = [NSString stringWithFormat:@"%lu", strtoul([_QQId UTF8String], 0, 16)];
                _QQKey = [keyDic objectForKey:@"qq"];
            } else if ([@"weixin"  isEqual: [dic objectForKey:@"CFBundleURLName"]]) {
                _WXId = [dic objectForKey:@"CFBundleURLSchemes"][0];
                _wxSecret = [keyDic objectForKey:@"weixin"];
            } else if([@"sina" isEqualToString:[dic objectForKey:@"CFBundleURLName"]]) {
                _SinaId = [dic objectForKey:@"CFBundleURLSchemes"][0];
                _SinaId = [_SinaId substringFromIndex:2];
                _SinaSecret = [keyDic objectForKey:@"sina"];
            }
        }
    } @catch (NSException *exception) {
        _QQId = @"";
        _QQKey = @"";
        _WXId = @"";
        _wxSecret = @"";
        _SinaId = @"";
        _SinaSecret = @"";
    } @finally {
        
    }
    return self;
}
@end
