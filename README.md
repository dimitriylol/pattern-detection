# Pattern detection

## Idea

    Analyse source code and highlight used design patterns. 
    
## Future realization

    It can be done in 3 steps:
    
    1. Parse source code for building AST (abstract syntax tree).
       AST is built by parser generator - instapase (https://github.com/Engelberg/instaparse). 
    2. Build the graph from AST for displaying UML relationships in analysed project. Used loom (https://github.com/aysylu/loom)
    3. Analyse graph for matching with design pattern.
    
## Future interface

    Console interface where user input is two pathes. 
    The first path is path to written example of design pattern.
    The second path is path to project.
    
    Result is some kind of map between real identifier in project and identifier from design pattern.
    
## HOWTO run

    You need to install lein environment. After that you can type 'lein run /absolute/path/to/src'
