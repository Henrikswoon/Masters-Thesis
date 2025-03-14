From the file _/TECHNICAL_

---

### Overview

Talos uses program analysis to identify existing error handling code in applications, synthesize SWRRs and find locations to insert SWRRs. It adopts a design that promotes the support for applications written in different programming languages and the ability to work with both source code and binary code. The key design choice is to separate the collection of information required to synthesize and insert SWRRs from the actual synthesis and insertion of SWRRs. A frontend is responsible for the former task while a backend is responsible for the latter task.

With this kind of design, Talos can have a single backend and multiple frontends to support source code in different programming languages and binary code for different platforms. Each frontend handles either source code in a particular programming language or binary code for a particular platform. Thus the backend can largely remain unchanged when for example we need to add support for source code in a new programming language.

### Frontend

CodeAnalyzer is the first frontend of Talos. It works on source code written in C/C++. It uses data flow analysis and control flow analysis to collect various information such as call graph, control dependency, function return value, and how the return value of a function call is checked by the program. To generate a complete call graph, CodeAnalyzer also keeps track of function calls via a function pointer. CodeAnalyzer is implemented as an analysis pass of LLVM. For a C/C++ source file, CodeAnalyzer outputs the collected information in text format, which is described in details in the MANUAL file. Choosing text format makes it easier to process the output of CodeAnalyzer with Python or other programming languages designed for rapid development.
    
CodeAnalyzer has been used to analyze 12 popular open-source applications such as HTTP servers, FTP server, database server, programming language interpreter, web browsers, and network protocol analyzer, each with 100,000 lines of source code on average. The following is the list of the applications that have been successfully analyzed by CodeAnalyzer.

- HTTP server/proxy: apache httpd, lighttpd, squid 
- FTP server: proftpd
- Database server/application: postgresql, sqlite
- Programming language interpretor: PHP
- web browser: firefox
- email client: evolution
- graphics application: inkscape
- messaging application: pidgin
- network protocol analyzer: wireshark

### Backend

The backend of Talos synthesize SWRRs and insert them into an application. It first identifies existing error handling code of the application based on the information collected by a frontend. Then it sythesizes SWRRs and inserts SWRRs into the application.

The backend is implemented in Python and currently it supports synthesizing and inserting SWRRs for source code only. 
