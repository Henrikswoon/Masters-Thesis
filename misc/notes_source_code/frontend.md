frontend/
└── CodeAnalyzer/
&emsp;&emsp;├── [Analyzer.cpp](#analyzercpp)
&emsp;&emsp;├── [Analyzer.cpp~](#analyzercpp)
&emsp;&emsp;├── dataflow/
&emsp;&emsp;│&emsp;&emsp;├── [bitvector.cc](#bitvectorcc)
&emsp;&emsp;│&emsp;&emsp;├── [bitvector.h](#bitvectorh)
&emsp;&emsp;│&emsp;&emsp;├── [cfg.h](#cfgh)
&emsp;&emsp;│&emsp;&emsp;├── [dataflow.cc](#dataflowcc)
&emsp;&emsp;│&emsp;&emsp;├── [dataflow.h](#dataflowh)
&emsp;&emsp;│&emsp;&emsp;├── [Makefile](#makefile)
&emsp;&emsp;│&emsp;&emsp;├── [reachacc.cc](#reachacccc)
&emsp;&emsp;│&emsp;&emsp;├── [reachacc.h](#reachacch)
&emsp;&emsp;│&emsp;&emsp;├── [reachdef.cc](#reachdefcc)
&emsp;&emsp;│&emsp;&emsp;└── [reachdef.h](#reachdefh)
&emsp;&emsp;├── [ipc_lock.cpp](#ipclockcpp)
&emsp;&emsp;├── [ipc_lock.h](#ipclockh)
&emsp;&emsp;├── [LICENSE](#license)
&emsp;&emsp;├── [Makefile](#makefile)
&emsp;&emsp;├── [README](#readme)
&emsp;&emsp;└── [TUTORIAL](#tutorial)

## `README`

#### Overview

CodeAnalyzer is easy to install and use. It includes several helper scripts such as C/C++ compiler wrappers to faciliate analyzing the source code of large projects without the need to modify the configuration of the projects. The `TUTORIAL` file demonstrates how to build CodeAnalyzer and use it to analyze the source code of `lighttpd`, a widely-used HTTP server. 


#### Build

 1. Copy the CodeAnalyzer directory to the project directory under the LLVM source code directory.

 2. Depending on the type of the LLVM build, e.g. Release+Asserts, modify the path specified by the INSTALLDIR variable in dataflow/Makefile.

 3. Run ‘make’ and ‘make install’ in data flow directory.

 4. Run ‘make’ and ‘make install’ in CodeAnalyzer directory.  

#### Usage

CodeAnalyzer can be started with the opt tool of LLVM. In the test directory, there is a shell script `runanalyze.sh`, which generates LLVM bitcode for a C/C++ source file and then runs ‘opt’ to execute CodeAnalyzer on the LLVM bitcode. It can easily be modified to  analyze any other C/C++ source file. To facilitate using CodeAnalyzer with a large project that consists of more than one C/C++ source files, CodeAnalyzer comes with a script wrapper for C/C++ compiler such as gcc/g++.  
By specifying this wrapper as the compiler when configuring a project or changing the order of paths in the PATH environment so that this wrapper will be used in place of the real gcc/g++, all the source files in a project can be analyzed by CodeAnalyzer without modifying its Makefile. 

#### Configuration

The path of the output file of CodeAnalyzer is specified with environmental variable ANALYZER_OUTPUT. 
Note that the output is appended to the file so that CodeAnalyzer can be used to collect information for more than one C/C++ source file of a large project.
  
CodeAnalyzer also uses a configuration file analyzer.cfg to allow an user to collect more information on demand. For example, the configuration file can specify the name of API functions and struct variables used by a program to perform important operations so that CodeAnalyzer will collect more information for calls to these functions and access to these variables. By default, CodeAnalyzer looks for analyzer.cfg in the current directory where it is executing. The path for analyzer.cfg can also be changed via environmental variable ANALYZER_CFG. The format of the configuration file is described in the example analyzer.cfg.

## Analyzer.cpp
Details about `Analyzer.cpp` go here.

## Analyzer.cpp~
Details about `Analyzer.cpp~` go here.

## `bitvector.h`
```c++
#include <vector>
#include <string>
class Bitvector{
    public:
        int *m_bits;
        int m_size; // size of m_bits
        int m_bsize; // size in bits
        Bitvector(int size);
        Bitvector(unsigned char a[], int size);
        Bitvector(const Bitvector& bv);
        ~Bitvector();
        int size() const;
        bool get(int bit) const;
        void set(int bit, bool val);
        void merge(const Bitvector& bv);
        void diff(const Bitvector& bv);
        void intersect(const Bitvector& bv);
        void print() const;
        Bitvector& operator=(const Bitvector& bv);
        bool operator!=(const Bitvector& bv);
        bool operator==(const Bitvector& bv);
        std::string tostring() const;
        unsigned char *toarray(int &size) const;
}
```


## `bitvector.cc`
#### Constructors
```c++
Bitvector::Bitvector(int size)
```
Allocates a _size_ number of 32 bit _words_ to _m_bits_

```c++
Bitvector::Bitvector(unsigned char a[], int size)
```
Allocates a _size_ number of 32 bit _words_ containing _a_ to _m_bits_

```c++
Bitvector::Bitvector(const Bitvector& bv)
```
Clones a _Bitvector_.
#### Deconstructor
Deallocates allocated field m_bits for deconstructed _Bitvector_

#### Helper Functions


---

## cfg.h
Details about `cfg.h` go here.

## dataflow.cc
Details about `dataflow.cc` go here.

## dataflow.h
Details about `dataflow.h` go here.

## Makefile
Details about `Makefile` go here.

## reachacc.cc
Details about `reachacc.cc` go here.

## reachacc.h
Details about `reachacc.h` go here.

## reachdef.cc
Details about `reachdef.cc` go here.

## reachdef.h
Details about `reachdef.h` go here.

## ipc_lock.cpp
Details about `ipc_lock.cpp` go here.

## ipc_lock.h
Details about `ipc_lock.h` go here.

## LICENSE
Details about `LICENSE` go here.


## TUTORIAL
Details about `TUTORIAL` go here.