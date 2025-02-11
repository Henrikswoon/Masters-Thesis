### File structure
backend/
├── [analyzer.cfg](#analyzercfg)
├── [partition_hotspot.py](#partition_hotspotpy)
├── [README](#readme)
├── samples/
│&emsp;&emsp;├── [proftpd.lines](#proftpdlines)
│&emsp;&emsp;└── [proftpd.out](#proftpdout)
├── [Talos_exec.c](#talos_execc)
├── [talos.py](#talospy)
└── [Talos_SWRR.c](#talos_swrrc)

## `analyzer.cfg`
Some configuration file? Purpose will hopefully become clearer soon.

## `partition_hotspot.py`
Not clear how it works. Perhaps clues will be given in other files.

## `README`
#### Overview
The backend of Talos synthesizes SWRRs and inserts SWRRs into applications. It takes as input two files produced by a front of Talos: one file contains information such as control flows, control dependencies, and function return values and one file contains miscellaneous information such as the number of lines and the starting line of each function.


#### Usage
The backend has several different functionality. The main functionality is synthesizing and inserting SWRRs. Among other functionality, the backend can also remove inserted SWRRs for debugging and other purposes. 

The backend is implemented in a Python program, talos.py. It takes various command line arguments to allow users to specify which functionality to perform and the values for different parameters. 

We use proftpd-1.3.5rc3 as a target application to illustrate how to use Talos to synthesize and insert SWRRs into the application and how to use Talos to remove the inserted SWRRs. We assume the source code of proftpd-1.3.5rc3 is stored at /home/user/code/proftpd-1.3.5rc3.
	
The input files are proftpd.out and proftpd.lines. The former contains mainly control flow information and the latter contains information on line numbers, both of which are generated automatically after you run analyze_app in the tools directory to collect information from proftpd. Note that you need to update the folder names used in analyze_app with the folder names of your application.

#### Synthesizing and inserting SWRRs

The command to synthesize and insert SWRRs into proftpd is as follows: "talos.py -S proftpd_restore_file -O proftpd.SWRRs -E main_/home/user/code/proftpd-1.3.5rc3/main.c proftpd.out 2>proftpd.err".

The "-S" option lets Talos synthesize and insert SWRRs. It takes an argument that specifies the output file for later removal of the SWRRs. The "-O" option specifies the output file for a list of SWRRs. 

And the "-E" gives the entry function of proftpd executable. Note that the frontend of Talso uses function name plus the pathname of the source file as the name particularly for C/C++'s main function and static functions so that these functions can be distinguished by the names even when static functions of the same name exist in different source files.

It is recommended to redirect the standard error to a file because Talos outputs a large amount of debugging information to standard error.

#### Removing SWRRs

The command to remove SWRRs from proftpd is as follows: "talos.py -R proftpd_restore_file -E main_/home/user/code/proftpd-1.3.5rc3/main.c proftpd.out"

Here we can see that the "-R" option tells Talos to remove SWRRs and it uses the specified removal file, which is generated when Talos runs with the "-S" option. This command will remove all SWRRs from an application. 


#### Activating SWRRs

Currently SWRRs protect at function levels. There are several reasons for that. One of the main reasons is that the name of a function that is responsible for a vulnerability is often readily available as soon as the vulnerability is discovered. So users can activate the SWRR corresponding to a vulnerable function to mitigate a vulnerability at that point, without any futher information about the vulnerability.

Each SWRR is activated via an SWRR option, which is implemented as an environment variable. And the name of each SWRR option identifies the name of the function that the option protects. For example, SWRR option "SWRR_pr_help_add_response" corresponds to function pr_help_add_response of proftpd. When Talos instruments an application, it generates a complete list of SWRR options for the application as a reference for users. As in the proftpd example, Talos stores the list of SWRR options in the proftpd.SWRRs file.

To activate an SWRR, a user simply needs to turn on the corresponding SWRR option by creating the environment variable. For example, a user creates an environment variable called SWRR_pr_help_add_response when starting the proftpd with instrumented SWRRs to activate the SWRR for function pr_help_add_response.



## proftpd.lines
This file is, as mentioned in the `README` section, one of 2 input files necessary to run Talos on proftpd.
## proftpd.out
Details about `proftpd.out` go here.

## Talos_exec.c
Details about `Talos_exec.c` go here.

## talos.py
Details about `talos.py` go here.

## Talos_SWRR.c
Details about `Talos_SWRR.c` go here.