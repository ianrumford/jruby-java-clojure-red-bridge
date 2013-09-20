
# Java to Ruby Red Bridge Example 2

require "java";

import 'java.util.HashMap';
import 'java.util.ArrayList';

$DEBUG = true

class Clojure2JRubyExample1

  WHOAMI = :Clojure2JRubyExample1
  
  def method1(*args)
    eye = :method1

    $DEBUG && args.each_with_index {|a, i| puts("#{WHOAMI}::#{eye} arg >#{i}< >#{a.class}< >#{a}<") }

    # return value must be a Ruby Hash
    returnValue = {"class" => self.class.name, "method" => eye.to_s}

    $DEBUG && puts("#{WHOAMI}::#{eye} returnValue >#{returnValue.class}< >#{returnValue}<")
    
    returnValue    
    
  end

  def method2(*args)
    eye = :method2

    $DEBUG && args.each_with_index {|a, i| puts("#{WHOAMI}::#{eye} arg >#{i}< >#{a.class}< >#{a}<") }

    rubyReturn = {"class" => self.class.name, "method" => eye.to_s}

    # retrun value must be a HashMap
    returnValue = HashMap.new(rubyReturn)

    $DEBUG && puts("#{WHOAMI}::#{eye} returnValue >#{returnValue.class}< >#{returnValue}<")
    
    returnValue
    
  end

  def method3(*args)
    eye = :method3
    
    $DEBUG && args.each_with_index {|a, i| puts("#{WHOAMI}::#{eye} arg >#{i}< >#{a.class}< >#{a}<") }

    rubyReturn = [self.class.name, eye.to_s]
    
    # return value must be an ArrayList
    returnValue = ArrayList.new(rubyReturn) # create an ArrayList of Java strings
    
    $DEBUG && puts("#{WHOAMI}::#{eye} returnValue >#{returnValue.class}< >#{returnValue}<")
    
    returnValue
    
  end

  def method4(*args)
    eye = :method4
    
    $DEBUG && args.each_with_index {|a, i| puts("#{WHOAMI}::#{eye} arg >#{i}< >#{a.class}< >#{a}<") }

    rubyReturn = [self.class.name, eye.to_s]
    
    # return value must be an Java array
    returnValue = rubyReturn.to_java
    
    $DEBUG && puts("#{WHOAMI}::#{eye} returnValue >#{returnValue.class}< >#{returnValue}<")
    
    returnValue
    
  end
  
end

# return an instance of the class
Clojure2JRubyExample1.new

__END__

puts ("CO")

#__END__

# Quick Testing

rbInst = Clojure2JRubyExample1.new

rbInst.method1 
rbInst.method2 :hello => "world"
rbInst.method3 :clojure => "rules!"
rbInst.method4 :the_rest => "sucks!"


__END__

