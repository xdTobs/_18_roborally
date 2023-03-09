# Markdown Conversion Guide

## Sources

<https://pandoc.org/MANUAL.html>

## Guide

### Install Pandoc

<https://pandoc.org/installing.html>

Depending on the ending you choose for you output file `Pandoc` can automatically choose the correct way to convert.

### To .pdf - PDF file

You might need to download a PDF engine, for example `xelatex`.

If no input-files are specified, input is read from stdin. Output goes to stdout by default. For output to a file, use the -o option:

```sh
pandoc  input.md -o output.pdf
```

### To .tex - latex file

`--standalone` creates a file that is complete. For an HTML file that means everything needed to follow the specification, `<head>`, `<html>`, `<title>`, `<body>`, etc. The same is true for a .tex file with the `--standalone` flag. Without this we only get the conversion of the elements in the file, but not a .tex document that can be compiled to pdf.

```sh
pandoc input-file.md --standalone --output output-file.pdf
```

### Latex header/preamble in Markdown

```
---
title: "RoboRally Taxonomy"
date: March 9, 2023
geometry: margin=3cm
header-includes: |
  \usepackage{fancyhdr}
  \pagestyle{fancy}
  \fancyfoot[CO,CE]{Group 18 - RoboRally Taxonomy - D. 3/8-2023}
  \fancyfoot[LE,RO]{\thepage}
---
```

Start the document with this to add things to the latex preamble.

### Author

by Henrik Zenkert
