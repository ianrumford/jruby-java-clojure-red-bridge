(ns redbridge.clojure-jruby
  
  (:require [clojure.tools.logging :as lgr])

  (:import (java.util List ArrayList Map HashMap)
           (java.io Reader FileReader BufferedReader)
           (org.jruby.embed ScriptingContainer)))


;; SCRIPT READER
;; *************


(defn new-script-reader
  "create a Java Reader pointing at the ruby script"
  [ruby-script]
  (let [file-reader (FileReader. ruby-script)
        buffered-reader (BufferedReader. file-reader)]
    buffered-reader))


;; SCRIPTING CONTAINER
;; *******************


(defn new-scripting-container
  "create a new jruby scripting container"
  []
  (org.jruby.embed.ScriptingContainer.))


(defn find-scripting-container
  [scripting-state]
  (if (contains? @scripting-state :container)
    (:container @scripting-state)
    (let [scripting-container (new-scripting-container)]
      (swap! scripting-state assoc :container scripting-container)
      scripting-container)))


;; SCRIPTING RECEIVER
;; ******************


(defn new-scripting-receiver
  "parse a ruby script and return a receiver"
  ([ruby-script] (new-scripting-receiver ruby-script (new-scripting-container)))
  ([ruby-script scripting-container]
     (let [scripting-receiver (.runScriptlet scripting-container (new-script-reader ruby-script) ruby-script)]
       scripting-receiver)))


(defn find-scripting-receiver
  [scripting-state]
  (if (contains? @scripting-state :receiver)
    (:receiver @scripting-state)
    (let [scripting-container (find-scripting-container scripting-state)
          scripting-receiver (new-scripting-receiver (get @scripting-state :script) scripting-container )]
      (swap! scripting-state assoc :receiver scripting-receiver)
      scripting-receiver)))


;; SCRIPTING STATE
;; ***************


(defn initialize-scripting-state
  "create a starter state"
  ([ruby-script] (initialize-scripting-state ruby-script nil))
  ([ruby-script ruby-method]
     (atom {:script ruby-script :method ruby-method})))


;; RUBY METHOD
;; ***********


(defn call-ruby-method
  "call the ruby method"
  [scripting-state result-class method-name & method-args]
  (let [scripting-container (find-scripting-container scripting-state)
        scripting-receiver (find-scripting-receiver scripting-state)
        scripting-name (if-not (nil? method-name)
                         method-name
                         (:method @scripting-state))]
    (if (not-empty method-args)
      (.callMethod scripting-container scripting-receiver scripting-name (object-array method-args) result-class)
      (.callMethod scripting-container scripting-receiver scripting-name result-class))))

