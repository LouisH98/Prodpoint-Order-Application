# 3D Printing Order Logging Application

#### Made for Prodpoint on a summer internship.

### Purpose
I was asked by the client to create an application that allows them to process and keep track of their orders.

Orders consist of [STL files](https://en.wikipedia.org/wiki/STL_(file_format)) (each file an object to be printed). Each file has information associated with it, such as:

* Quanitity
* Material
* Colour
* Resolution
* Additional Notes
  
The client also wanted a visual display of each file allowing them to identify each object in the order.
The application should also be cross platform

## Implementation
### Technology
#### Language
My initial plan was to use [Electron](https://www.electronjs.org/), which is essetially a Nodejs server with a Chromium frontend, both using JavaScript as the scripting language. 

However at the time I had a lot more experience developing in Java, especially with an object oriented approach.
This led me to choose the JavaFX framework to make the GUI for the application, with Java as the language.

#### STL Rendering
I had found some interesting tools to view STL files with, but they had a little too much functionality, such as 3D viewing and other tools. 
I then found a Windows application that renders STL files to an .ico file to allow STL files to have a visible thumbnail. 
This application also supports command-line usage, meaning that I could specify which files I wanted an image of; perfect for my use-case!

#### Recording Orders
The users settings are recorded in XML, with the key as the filename and setting, and the value as the user setting, here's an example:
```xml
<entry key="clientName">Louis</entry>
<entry key="M3_System20 Bracket 24-40mm Single Sided Right.stl.resolution">0.1</entry>
<entry key="M3_System20 Bracket 24mm Single Sided.stl.plasticType">PLA</entry>
<entry key="M3_System20 Bracket 24-40mm Single Sided Left.stl.notes"/>
<entry key="M3_System20 Bracket 40-40mm Single Sided Square.stl.quantity">1</entry>
<entry key="M3_System20 Bracket 20mm Single Sided.stl.plasticType">PLA</entry>
<entry key="M3_System20 Bracket 28mm.stl.colour">Black</entry>
```

This allows for a really fast way of storing user data, without requiring any sort of database management system.
It also plays nice with the Java Preferences API, which essetially acts as a Map, but allowing ease saving and loading of data.

# Screenshots
**A typical order**
![typical order](/img/main.png)

**Customising Colour**
![changing colour](/img/colours.png)

**Small Order**
![changing colour](/img/single.png)

# GIFs

**Creating an order**
![creating order](/img/create-order.gif)

**Changing settings**
![changing settings](/img/change-settings.gif)
