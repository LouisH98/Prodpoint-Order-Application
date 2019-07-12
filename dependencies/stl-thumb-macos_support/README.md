# stl-thumb

[![Build Status](https://travis-ci.org/unlimitedbacon/stl-thumb.svg?branch=master)](https://travis-ci.org/unlimitedbacon/stl-thumb)
[![Build Status](https://ci.appveyor.com/api/projects/status/exol1llladgo3f98/branch/master?svg=true)](https://ci.appveyor.com/project/unlimitedbacon/stl-thumb/branch/master)

Stl-thumb is a fast lightweight thumbnail generator for STL files. It can show previews for STL files in your file manager on Linux and Windows. It is written in Rust and uses OpenGL.

![Screenshot](https://user-images.githubusercontent.com/3131268/42529042-31d9bca6-8432-11e8-9ba8-87d9b72aaddb.png)

## Installation

### Windows

Stl-thumb requires 64 bit Windows 7 or later. [Download the installer .exe](https://github.com/unlimitedbacon/stl-thumb/releases/latest) for the latest release and run it.

The installer will tell the Windows shell to refresh the thumbnail cache, however this does not always seem to work. If your icons do not change then try using the [Disk Cleanup](https://en.wikipedia.org/wiki/Disk_Cleanup) utility to clear the thumbnail cache.

### Mac
Make sure cargo and rust are installed
Navigate to the folder and run:
cargo install

## Command Line Usage

```
$ stl-thumb <STL_FILE> [IMG_FILE]
```

### Options

| Option        | Description                                             |
| ------------- | ------------------------------------------------------- |
| <STL_FILE>    | The STL file you want a picture of.                     |
| [IMG_FILE]    | The thumbnail image file that will be created. If this is omitted, the image data will be dumped to stdout. |
| -s, --size \<size\>   | Specify width of the image. It will always be a square. |
| -f, --format \<format\> | The format of the image file. If not specified it will be determined from the file extension, or default to PNG if there is no extension. Supported formats: PNG, JPEG, GIF, ICO, BMP |
| -m, --material \<ambient\> \<diffuse\> \<specular\> | Colors for rendering the mesh using the Phong reflection model. Requires 3 colors as rgb hex values: ambient, diffuse, and specular. Defaults to blue. |
| -b, --backround \<color> | The background color with transparency (rgba). Default is ffffff00. |
| -x            | Display the image in a window.                          |
| -h, --help    | Prints help information.                                |
| -V, --version | Prints version information.                             |
| -v[v][v]      | Increase message verbosity. Levels: Errors, Warnings, Info, Debugging |
