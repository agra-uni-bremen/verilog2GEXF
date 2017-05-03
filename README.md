# Verilog2GEXF

A free Verilog-netlist to GEXF converter

## Purpose

Current and state of the art hardware visualizers are typically embedded to modern, complex and most prominently proprietary design tools. The dedicated development of a free and universal visualization tool is troublesome since it typically needs to reflect several important properties of
circuit descriptions and the intended use-cases. We propose a transformation of synthesized Verilog netlists to the Graph Exchange XML Format, which can be processed by a variety of tools, which are freely available, thus adding several ways of circuit visualization.

### Publication

The idea was presented on the 4th Workshop on Design Automation for Understanding Hardware Designs in Lausanne in 2017 as work in progress with the title "Verilog2GEXF Dynamic Large Scale Circuit Visualization"

## Usage

* The project uses Gradle as build-system
* Run `$ gradle build` to generate the executable (gradle is required)
* You may move the generated Verilog2Gexf.jar to any other location
* A Verilog netlist must be specified (e.g. `java -jar Verilog2Gexf.jar /path/to/file.v`)
* The resulting *.gexf-file will be dumped to the same directory as the initial netlist

## Technology & Limitations

* [ANTLR4](http://www.antlr.org/) (4.5.3) is used to generate the parser backend
* A reduced version of the original [Verilog2001](https://github.com/antlr/grammars-v4/tree/master/verilog) grammar (*.g4) was used to create the parser
* Primitive gate-netlists can be processed (relying on logic primitves such as and, or, nand, nor, etc.)
* The software is tested against ISCAS '85 and '89 circuits [Source](http://www.pld.ttu.ee/~maksim/benchmarks/)

## License

- MIT License


