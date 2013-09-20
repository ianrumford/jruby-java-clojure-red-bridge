(ns redbridge.blog1
  
  (:require [clojure.tools.logging :as lgr]
            [redbridge.clojure-jruby :as cjrb]
            [clojure.string :as string])

  (:import (java.util List ArrayList Map HashMap)
           (org.jruby RubyHash)
           )
  
  )

;; Clojure2JRubyExample1
;; *********************

;; Ruby script path
(def ruby-script-clojure2jruby-example1 "./src/main/ruby/bin/clojure2jruby_example1.rb" )

;; Initialize the state atom
(def state-clojure2jruby-example1 (cjrb/initialize-scripting-state ruby-script-clojure2jruby-example1))

;; method1: no arguments, returns a Ruby Hash
;; *******

(def c2j-m1 (cjrb/call-ruby-method state-clojure2jruby-example1 RubyHash "method1"))
;; =>
{"class" "Clojure2JRubyExample1", "method" "method1"}

;; Can iterate over the returned Ruby Hash
(for [[k v] c2j-m1] (lgr/info (format "k >%s< >%s< v >%s< >%s" (class k) k (class v) v)))
;; => Logging on the REPL console

;; common map function

(keys c2j-m1)
;; => 
'("class" "method")

(vals c2j-m1)
;; =>
'("Clojure2JRubyExample1" "method1")

(count c2j-m1)
;; =>
2

(into {} (map (fn [[k v]] [(string/capitalize k) (string/upper-case v)]) c2j-m1))
;; =>
{"Class" "CLOJURE2JRUBYEXAMPLE1", "Method" "METHOD1"}

(defn -main
  "main method to run a simple demo"
  [& args]
  (cjrb/call-ruby-method (cjrb/initialize-scripting-state "./src/main/ruby/bin/clojure2jruby_example1.rb") RubyHash "method1" args))

