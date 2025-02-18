# twit-the-config 
# Tech Stack
* Java 11
* Mockito
* Junit jupiter
* assertJ

# Functionalities Covered
* Ability to handle Groups.
* Ability to handle empty lines.
* ABility to handle overrides.

# Running the project
`./gradlew build`
You will see the jar in the target folder. You need imoport this jar into your project in order to start working.

# Approach
In this implementation we have a generic **Parser<T,K>** interface, where T is the input and K is the parsed output. This interface is being implemented by **PatternMatchingParser**. Its an abstract class which handles the pattern macthing and parses the input accordingly. It is being implemented by **PropertyParser** and **GroupParser**. Both the classes provide their regex to the PatternMatchingParser and calls their super class to match the pattern. Both the parsers implement the parse method of Parser interface.

By the above approach even if we want to add another parser which parses based on regex, we just need add another class and pass the regex to the base class and wire it wherever needed. And if we want to add another parser which is not based on regex. We can directly implement the Parser interface.

The PropertyParser takes a line (String) as input and gives out the Prop object. It matches the line and throws the **ConfigFormatException** if the regex is not matching. It also verifies if the property is an overriden one. The **Props** class holds the default value and overriden values. The Props is extended by OverriddenProps class. The OverriddenProps props class stores, default and override key and value. The parser when discovers an overridden value, return the OverriddenProps object and when a default value, it returns Props object.
Similarly the **GroupParser** takes care of the group. It takes stream of lines and breaks them into Groups. A Group consists of a name and list of Props. The Group parser when finds a group, combines all the lines below it till it finds another group. The GroupParser also calls the propertyParser as it process a line and stores the property to group. 

The Group is smart enough to undertsand if an Overridden property is coming in. If yes it adds to the existing props.
Once all the groups are parsed the list of groups are returned to the caller. In this case its ConfigLoader. The ConfigLoader is the master class exposed to public. The loader takes both the parsers and a FileReader in its constructor. It exposes a method loadConfig which takes file location and list of active overrides. The method returns the Config object. The config object holds a map of String and Map String, Object i.e **Map<String, Map<String, Object>>**. Its maps group name as key and then props and another map.
It exposes a method add block addBlock() which the ConfigLoader calls, the method prepares the map by calling props to give only values related to overrides. The Props class takes care of setting the value based on the availibility of the particular active overrides. In case no value found it returns the default one. The Props assumes that the overrides will be defined only after the defaults are. 

All the scenarios are well tested using junit jupiter unit tests and 1 integration test.

# Areas of Improvement
The design implemented was one of the options came to mind. However there can be alternatves as well. In this implementation, the configLoader calls GroupParser and the GroupParser calls the PropertyParser. We can also do in other way where groupParser maps to the raw Groups which are group name and list of lines which is again mapped to propertyParser  by outside class.
Like: `Stream<Lines> readLines = fileReader.read()
groupParser.parse(readLines).map(PropsParser.parse).map(PropertyParser.parse).forEach(addBlock)`

Comments are not handled yet. The filter for empty line is on ConfigLoader. May be on the similar approach of parsers we can also have different filters which would be applied before calling the parser.

The Config class has a map. I could have introduced another concrete class in order to control it better.

Another set of improvement is the contract for the Parser. The parser exposes an interface which has 1 method as contract.
* Parse() - responsible to parse.
Currently the classes extending from PatternMatchingParse, uses the matches method internally while parsing. May be we can expose matches as a contract with the Parser interface and then let the caller first call the macthes before calling parse. That will make the GroupParser and Property parser more cleaner. Also this goes well with our very first suggestion for improvement.

The other approach, which might be more optimized for large files, would be creating a parsing tree. Where root node would be all lines and then level one could be all groups and level 2 could be all props under groups, level 3 could be overrides. THis approach is used in case of grammar parsing, like programming languages. I thought this would be too much for this lebel of problem hence didn't applied.

