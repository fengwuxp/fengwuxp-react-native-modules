require "json"

Pod::Spec.new do |s|
  # NPM package specification
  package = JSON.parse(File.read(File.join(File.dirname(__FILE__), "package.json")))

  s.name         = "fengwuxp-react-native-mob-share"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.homepage     = "https://github.com/fengwuxp/fengwuxp-react-native-modules/tree/master/packages/mob-share"
  s.license      = "MIT"
  s.author       = { package["author"]["name"] => package["author"]["email"] }
  s.platforms    = { :ios => "9.0", :tvos => "9.0" }
  s.source       = { :git => "https://github.com/fengwuxp/fengwuxp-react-native-modules.git"}
  s.source_files = "packages/mob-share/ios/**/*.{h,m}"
 
  s.ios.dependency 'mob_sharesdk'

  # UI模块(非必须，需要用到ShareSDK提供的分享菜单栏和分享编辑页面需要以下1行)
  s.ios.dependency 'mob_sharesdk/ShareSDKUI'
  
  # 平台SDK模块(对照一下平台，需要的加上。如果只需要QQ、微信、新浪微博，只需要以下3行)
  s.ios.dependency 'mob_sharesdk/ShareSDKPlatforms/QQ'
  s.ios.dependency 'mob_sharesdk/ShareSDKPlatforms/SinaWeibo' 
  s.ios.dependency 'mob_sharesdk/ShareSDKPlatforms/WeChatFull'  
  s.ios.dependency 'mob_sharesdk/ShareSDKPlatforms/TencentWeibo'
  s.ios.dependency 'mob_sharesdk/ShareSDKExtension'
  s.dependency "React"

end
