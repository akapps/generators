# Generators library

The goal of this library is to not rewrite again and again the same code to generate
fixtures (code, scripts like SQL, etc.)

The exact features list is actually not defined, but includes :
  - generate Java-based fixtures from DB, based on JPA introspection
  - generate Java objects based on SQL scripts and reverse
  - ...


## First target : generate code from object

&rarr; Given an object, generate code to recreate it
  - `[X]` (1) use only standard Java-bean setters with no Method validation
  - `[X]` (1) use only standard Java-bean-like empty constructor (with no validation)
  - `[X]` (1) print in console
  - `[ ]` (opt) print in file ? Append to file ?
  - `[ ]` (2) use builder syntax
  
  _(Maybe here I would like to work on another feature, like reading from DB / SQL script / ??)_
  
  - `[ ]` (3) validate setters methods using Reflection, validate Constructor as well
  - `[ ]` (4) try find alternative Constructor / setter ?