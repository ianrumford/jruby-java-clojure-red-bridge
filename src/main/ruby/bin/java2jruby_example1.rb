
# Java to Ruby Red Bridge Example 1

require "java";

import 'java.util.HashMap';

$DEBUG = true

class Java2JRubyExample1

  WHOAMI = :Java2JRubyExample1

  def method1(*args)
    eye = :method1

    $DEBUG && args.each_with_index {|a, i| puts("#{WHOAMI}::#{eye} arg >#{i}< >#{a.class}< >#{a}<") }

    rubyReturn = {"class" => self.class.name, "method" => eye.to_s}
    
    # java expects a HashMap
    returnValue = HashMap.new(rubyReturn)

    $DEBUG && puts("#{WHOAMI}::#{eye} returnValue >#{returnValue.class}< >#{returnValue}<")
    
    returnValue
    
  end
  

end

# return an instance of the class
Java2JRubyExample1.new

__END__
d

  

