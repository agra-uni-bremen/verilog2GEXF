grammar Verilog2001_netlist;

@header {
package de.unibremen.agra.gexf.generated;
}

Comma
   : ',' -> skip
   ;

LINE_COMMENT
    : '//' ~[\r\n]* -> skip
;

module_declaration
   : 'module' module_identifier '(' list_of_ports ')' ';' module_item* 'endmodule'
   ;

module_identifier
   : identifier
   ;

list_of_ports
   : port_identifier (port_identifier)*
   ;

list_of_cell_ports
   : port_identifier (port_identifier)*
   ;

port_identifier
   : identifier
   ;

module_item
   : inputs
   | outputs
   | wire
   | gate
   ;

inputs
   : 'input' list_of_ports ';'
   ;

outputs
   : 'output' list_of_ports ';'
   ;

wire
   : 'wire' list_of_ports ';'
   ;

gate
   : logic_primitives primitive_identifier '(' list_of_ports ')' ';'
   ;

primitive_identifier
   : identifier
   ;

logic_primitives
   : 'and'
   | 'or'
   | 'nand'
   | 'nor'
   | 'xor'
   | 'xnor'
   | 'not'
   | 'buf'
   | 'dff'
   ;

White_space
   : [ \t\n\r] + -> channel (HIDDEN)
   ;

identifier
   : Simple_identifier
   | Escaped_identifier
   ;

Simple_identifier
   : [a-zA-Z_] [a-zA-Z0-9_$]*
   ;

Escaped_identifier
   : '\\' ('\u0021'..'\u007E')+ ~ [ \r\t\n]*
   ;
