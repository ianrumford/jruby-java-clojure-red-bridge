(defproject redbridge "0.1.0-SNAPSHOT"
  :description "Calling jRuby from Java and Clojure using Red Bridge"
  :url "https://github.com/ianrumford/jruby-java-clojure-red-bridge"
  :license {:name "MIT"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.5.1"]

                 [org.clojure/tools.logging "0.2.6"]
                 
                 [org.slf4j/slf4j-api "1.7.5"]
                 [ch.qos.logback/logback-classic "1.0.13"]
                 [ch.qos.logback/logback-core "1.0.13"]

                 [org.jruby/jruby-core "1.7.4"]
                 ;;[org.jruby/jruby-complete "1.7.4"]                

                 [net.sf.jopt-simple/jopt-simple "4.5"]
                 
                 ]

  :source-paths ["src/main/clojure"]
  :java-source-paths ["src/main/java"]

  ;;:aot :all
  
  )