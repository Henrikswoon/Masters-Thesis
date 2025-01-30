----> slide 1<br/>
__Title:__<br/>
This presentation will serve as a summary to Talos: Neutralizing vulnerabilities with Security Workarounds for Rapid Response. Focusing on the parts which i have deemed more important to the work i will be doing extending it's functionality and further strengthening claims made.

<div style="page-break-before: always;"></div> 
----> slide 2<br/>
__Introduction:__<br/>
There is often a considerable delay between the discovery of a vulnerability and the issue of a patch. According to findings made in this article 43.4% of vulnerabilities inspected were patched within 7 days after the vulnerabilities were disclosed, 23.3% between 7 and 30 days and 33.3% of them were patched after 30 days. We can assume that the number of exploits performed should correlate to lenght of time between discovery and patching. 

One way to mitigate this window of vulnerability is through configuration workaround, which prevents vulnerable code from being executed.

<div style="page-break-before: always;"></div> 
----> slide 3<br/>
The Android Stagefright bug is provided in the article as an example where this technique could be applied. 

Stagefright was a remote code execution vulnerability which affected almost 1 billion devices which was discovered April 2015, publicly disclosed July and patched August the same year. And 1 year later many devices still run this vulnerable code.

The stagefright exploit abused automatic media download in MMS, which and was easily preventable by configuring the MMS client to not download any media automatically.

<div style="page-break-before: always;"></div> 
----> slide 4<br/>
This would allow us to ensure Confidentiality and Integrity at the cost of Availability. However such configurations are not always available. Findings show that between 6 widely distributed softwares Internet explorer had the highest coverage of 54.8% and a weighted average shows that only 25.2% of vulnerabilities can be neutralized with a configuration workaround. These workarounds also often cause 'collateral damage', disabling more functionality than what is necessary

<div style="page-break-before: always;"></div> 
----> slide 5<br/>
"Motivated by the problems of the pre-patch vulnerability window and the low coverage of configuration workarounds, we propose _Security Workarounds for Rapid Response"_ - Zhen Huang et. al.

These workarounds abbreviated as SWRRs are essentially configurations programmatically inserted at discovered error handling code segments. Which should improve coverage compared to traditional configuration workarounds without being costly for software developers. This discovery and insertion is implemented in the software named Talos.
 
<div style="page-break-before: always;"></div> 
----> slide 6<br/>
The articles goals are then

 1. Providing a low-cost way for software developers to quickly protect users during the pre-patch vulnerability window.
 2. Designing and implementing a sofware tool called Talos to demonstrate that SWRRs can be practically deployed. 
 3. Evaluating the effectiveness and coverage of the SWRRs inserted by Talos into 5 popular applications

<div style="page-break-before: always;"></div> 
----> slide 7<br/>
Talos can be broadly generalized as a 2 part algorithm

*1*. Find 'error handling code' defined as code whose purpose is to handle unexpected or abnormal error conditions. 

<div style="page-break-before: always;"></div> 
----> slide 8<br/>
Error handling code comes in 2 different categories intra-procedural which operate completely within a function, and inter-procedural which are unable to handle the error within the function and must expose the error to the function's caller. 

Intra-procedural error handling is difficult for Talos to utilize due to their coupling with the path used to invoke the error handling path. 

For example. They may free memory that they know was allocated on the path leading to the error-handling code, or conversely fail to free memory since they know the path leading to the error-handling code did not allocate it. 

Talos redirecting execution to such an error-handling path without regard to internal semantics could result in a double free bug. 

Inter-procedural error-handling code that exposes the error to the caller must be more conservative because it must be written in such a way that correctness guarantees are met independently of the calling context. 

As a result, such error handling code often seeks to ensure that modifications made to application state by the function are undone and that an appropriate value is returned to the caller so that the caller can then handle the failure.

For example, an input sanitization function that fails due to an out-of memory error might free any resources aquired up to that point and then return an error code to the caller so that the caller can conservatively reject the unsanitized input.

<div style="page-break-before: always;"></div> 
----> slide 9<br/>
 There are 2 main heuristics to find 'error handling code'

 1. Error-logging function heuristic
 > By identifying error-logging functions we can assume that any function calling these is 'error-handling'
 2. <code>NULL</code> return heuristic
 > Functions which usually return a pointer but in specific cases return a <code> NULL </code> pointer are assumed to have failed and to be communicating this by returning a <code> NULL </code> pointer. However, some extra precautions are necessary in this case to ensure that a null dereferencing error isn't caused.

Further, there are some exension heuristics
 
 1. Error propagation heuristic
 > Given a caller and calle, if the calle returns an error code and the caller itself passes this further up the control flow they're identified as error propagating functions even if error codes are translated during propagation.
 2. Indirect heuristic
 > Any error handling function which has been identified as being possible to disable by disabling one or multiple other functions are instead of being instrumented themselves mapped to these error handling functions instead.

<div style="page-break-before: always;"></div> 
----> slide 10<br/>
> *2*. The second step in the algorithm is Instrumenting the code such that it can be toggled using either in-place deployment or patch-based deployment.  

 - 'in-place deployment' In this case, Talos is run on the entire code base before it is released. Talos inserts an SWRR *check* into every function function in the application. Each SWRR check reads and checks a corresponding SWRR option in an accompanying SWRR configuration file. This allows the application developer to selectively disable code in an application without having to replace the binary by pushing out a new SWRR configuration file instead. Alternatively, the user may change the configuration file to enable the appropriate SWRR if they know which function the vulnerability occurs in.

 'in-place deployment' is appropriate when runtime performance is not critical or when updating binaries is difficult.

 'patch-based deployment' In this case, the application developer will run Talos on the application code base when they learn of a new vulnerability, passing Talos the information it requires about the vulnerability. Talos will then insert code that disables vulnerable functions by triggering error-handling code to return the application to a 'good' state.

 'patch-based deployment' has no code size or runtime overhead, but requires new binary code to be distributed and installed.

<div style="page-break-before: always;"></div> 
----> slide 11<br/>
Talos has some notable limitations and assumptions which should be mentioned before proceeding. 

The main assumption made is that the software implements a sufficient amount of error-handling code which is appropriate for instrumentation.

An important limitation to mention is that, Talos will not guarantee that generated SWRRs do not impact Availability as this is deemed acceptable even though it createes a potential denial-of-service vulnerability. This is similar to other vulnerability mitigation techniques such as ASLR, CFI or non-executable stacks which cause crashes upon attempted exploitation.

<div style="page-break-before: always;"></div> 
----> slide 12<br/>
Let's discuss the cost of implementing Talos as being cost efficient was a stated goal.

Authors argue that Talon is comparatively significantly cheaper than writing a patch since a developer must both gain a good understanding of the code causing the vulnerability, and why it's causing a vulnerability if they do not already have this knowledge and design / reimplement desired functionality without this vulnerability. Assuming Talos covered this vulnerability it only requires to know which function caused the vulnerability which can often be obtained by inspecting logs. The difference in effort is then dependent on the complexity of the vulnerability in question.


<div style="page-break-before: always;"></div> 
----> slide 13<br/>
In summary Talos should provide
 - Security: Generated SWRR should neutralize it's intended vulnerability, without introducing new bugs or vulnerabilities which affect Integrity or Confidentiality.
 - Effective coverage: Generated SWRRs should be able to cover a significant amount of vulnerabilities compared to configuration workarounds. This is measured in two components
  * 'basic coverage' The number of vulnerabilities whose code SWRRs can disable
  * The percentage of SWRRs that when enabled, result in a minor loss of functionality, similar to what would be expected from a configuration workaround.
 - Low Cost: SWRRs are automatically inserted into an application using Talos, thus minimizing the engineering effort required to user SWRRs.

What were the results then?
> Out of 11 CVE's chosen all was successfully neutralized, 8 och which was deemed to be unobtrusive. 

> A average 3 out of 4 error handling path's could be identified with a matching error code.

> Obtrusiveness measured as being able to process 'expected input' when SWRRs are enabled showed that a weighted average out of 64 SWRRs 71.3% was deemed Unobtrusive.

> Overhead was measured for in-place deployment, since patch based deployment would produce negligible overhead. It found that project lines of code increased on average 2% changing 89% of the files. Runtime performance overhead was measured at 1.3%.

These results show promise in providing Security, Effective coverage at a Low Cost.


