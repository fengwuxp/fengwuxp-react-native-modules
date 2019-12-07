require "json"

Pod::Spec.new do |s|
  # NPM package specification
  package = JSON.parse(File.read(File.join(File.dirname(__FILE__), "package.json")))

  s.name         = "RNGestureHandler"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.homepage     = "https://github.com/fengwuxp/fengwuxp-react-native-modules/tree/master/packages/unified-pay"
  s.license      = "MIT"
  s.author       = { package["author"]["name"] => package["author"]["email"] }
  s.platforms    = { :ios => "9.0", :tvos => "9.0" }
  s.source       = { :git => "https://github.com/fengwuxp/fengwuxp-react-native-modules/tree/master/packages/unified-pay", :tag => "#{s.version}" }
  s.source_files = "ios/**/*.{h,m}"

  s.dependency "React"

end
