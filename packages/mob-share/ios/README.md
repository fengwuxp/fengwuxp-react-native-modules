分享设置
具体查看文档
http://www.mob.com/wiki/detailed?wiki=ShareSDK_ios_fast_integration_fast&id=14
1、在项目工程的Info.plist中如图增加MOBAppKey 和 MOBAppSecret 两个字段
2、配置URL Scheme 注意查看以下文档链接 http://www.mob.com/wiki/detailed?wiki=ShareSDK_ios_urlscheme_two&id=14
3、在info.plist文件的ThirdKey中分别配置分享的key
http://bbs.mob.com/forum.php?mod=viewthread&tid=275&page=1&extra=#pid860
4、执行命令 yarn add    （分享）
5、在ios 工程目录下的Podfile文件中加入
pod 'mob_sharesdk' 
pod 'mob_sharesdk/ShareSDKUI' 
pod 'mob_sharesdk/ShareSDKPlatforms/QQ'
pod 'mob_sharesdk/ShareSDKPlatforms/SinaWeibo' 
pod 'mob_sharesdk/ShareSDKPlatforms/WeChatFull'  
pod 'mob_sharesdk/ShareSDKPlatforms/TencentWeibo'
pod 'mob_sharesdk/ShareSDKExtension'

pod 'fengwuxp-react-native-mob-share', :path => '../node_modules/fengwuxp-react-native-mob-share/ios/ShareModule'
6、执行命令pod install
