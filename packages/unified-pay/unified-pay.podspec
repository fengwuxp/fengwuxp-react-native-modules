require "json"

Pod::Spec.new do |s|
  # NPM package specification
  package = JSON.parse(File.read(File.join(File.dirname(__FILE__), "package.json")))

  s.name         = "unified-pay"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.homepage     = "https://github.com/fengwuxp/fengwuxp-react-native-modules/tree/master/packages/unified-pay"
  s.license      = "MIT"
  s.author       = { package["author"]["name"] => package["author"]["email"] }
  s.platforms    = { :ios => "9.0", :tvos => "9.0" }
  s.source       = { :git => "git@github.com:fengwuxp/fengwuxp-react-native-modules.git", :tag => "#{s.version}" }
  s.source_files = "tree/master/packages/unified-pay/ios/**/*.{h,m}"

  s.ios.dependency 'WechatOpenSDK', '~> 1.8.6'
  s.dependency "React"

end
