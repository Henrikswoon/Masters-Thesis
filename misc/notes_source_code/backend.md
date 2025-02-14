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

## `talos.py`
Entry at 
```python
if __name__ == '__main__':
    start_time = time.time() # Used to record and display total runtime for synthesis and instrumentation
    
    parser = argparse.ArgumentParser()
    parser.add_argument(...)
    args = vars(parser.parse_args(sys.argv[1:])) 

    talos = Talos(args) # Run pass 1-4
    talos.main()        # Later
```

```python
class Talos:
    def __init(self, args):
    # Initialize objects
    # Receive input
2863: self.pass1(self.InputFile) # InputFile in this case is 
```

```python
def pass1(self, InputFile):
    print "pass 1...initialize
    countWrappers = 0
    lineNo = 0
    input = open(InputFile, 'r')
    for line in input:
        # 1.  Increment lineNo
        # 1.1 Print LineNo and if Debug flag is active for LineNo
        # 2.  Strip and Split line from input file at Field separator '@' characters as parts[] 
        # 3.  Get function name from parts[4] and store it as functionName
        # 4.  If functionName is '*' print 'Ignoring * as caller at {lineNo}'
        # 5.  Get Callee name from parts[7] and store it as calleeName
        # 6.  Try to get the functionID using get_function(self, dir, file, function):
        # 6.{
            # get_function() either returns an unique id (int) if the function exists in self.Functions
            # if the function was not previously appended to self.Functions 
            #     then do so using key (dir, file, function)
            #     store the reverse in self.FunctionRef[ID] = (dir, file, function)   
            # finally, if the function is not in FunctionLocaion
            #     FunctionLocation[function] = set().add(ID)
            # return ID   
        #   }
        # 7. get fileId (int) from get_file(parts[1], parts[2]) directory / filename
        # 8. get Basic Block (int) for LineNo using self.get_BB(fileID, functionID, parts[3], parts[5], calleeName) 3:lineNum / 5:BBID
        # 9. if calleeName:
        #       if not calleName in self.RCalls: # Where RCalls stores a set of Basic Blocks
        #           assign an empty set to RCalls[calleeName]
        #       self.RCalls[calleeName].add(BB)
        #       if not functionName in self.Calls
        #           assign an empty set to Calls[functionName]
        #       self.Calls[functionName].add(BB)
        #       self.Callees[BB] = calleeName
        #       self.LineOfCall[(fileID, parts[3])] = BB # 3:lineNum
        # 10. if parts[10].find(',') > 0: 
        #       retInfo = parts[10].split(',')
        #       callType = int(retInfo[0])
        #       ret_line = int(retInfo[1])
        #       ret_value = retInfo[2]
        #       ret_BB = self.get_BB(fileID, functionID, str(ret_line), str(ret_line))
        #       self.BBFollowByReturn[BB] = (ret_BB, ret_value, fileID)
        #
        #     if ret_value == "NULL":
        #       if not functionName in self.FunctionLines:
        #           self.FunctionLines[functionName] = [0,0, -int(parts[3]),None,None,None]     # 3:lineNum
        #       else: # if functionName in self.FunctionLines
        #           self.FunctionLines[functionName][2] = -int(parts[3],None,None,None)         # 3:lineNum
        #       self.add_error_return(3, functionName, BB, ret_value)   # arg[0] is 'error return kind'
        #                                                               # 3:pointer returning
        #       
        #       
```
## Talos_SWRR.c
Details about `Talos_SWRR.c` go here.