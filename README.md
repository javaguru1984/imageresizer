# Description

Image resizing program. 

Realize 2 algorithms of compression: a) primitive pixel shrink algorithm, b) based on library `imgscalr` algorithm 

# Notice

This program is compiled with `OpenJDK 21` option. 

# Run program

To run image resizer, you can just execute command:

```bash
java -jar ImageResizer.jar -i <InputFolder> -o  <OutputFolder>  
```

In this way, program compress all photos from input folder defined with `-i` option, and put these images to the output folder, set up by `-o` option. 

By default, primitive compression is used. 

You can set up `imgscalr` library algorithm with option:  `-imgscalr`

```bash
java -jar ImageResizer.jar -i <InputFolder> -o <OutputFolder> -imgscalr
```

## Program options

- -i     (--input)
Determine input folder with images to compress.

- -o     (--output)
Determine output folder to store compressed images.

- -imgscalr
Determine `imgscalr` algorithm to compress input files. 

By default, use primitive shrink algorithm.

## Run program with parameters

Run program with default primitive algorithm:

```bash
    java -jar ImageResizer.jar -i <InputFolder> -o <InputFolder>
```

Run program with `imgscalr` library compress algorithm:
```bash
    java -jar ImageResizer.jar -i <InputFolder> -o <InputFolder> -imgscalr
```
