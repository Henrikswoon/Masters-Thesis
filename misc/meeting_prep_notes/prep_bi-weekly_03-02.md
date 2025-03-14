Introduce Christopher

Planned Work:
    - Forward snowballing analysis.
      I didn't find the sources gained from a Forward snowballing analysis to be particularily relevant to this thesis.
    - Presentation on reviewed litterature
      I will hold a presentation on Talos 
    - Proposal on a programming language
      I propose Java, as it's a language which I have prior experience in and some specific functions such as Agents could be interesting.
    - Begin writing 'Background' and 'Related works'
      I have written some basic introductory text and set up the environment. Moving forward i aim to write 1-2 pages a day including summaries or notes.

Presentation on Talos

Proposal Algorithm for Java.
```
procedure FIND_FUNCTIONS(Functions)
    to_instrument <-- Ø
    SWRR_map <-- Ø
    for f in Functions do
        //Main Heuristic
        if 'catch' in f then
            to_instrument <-- {f, error_code(f)}
            SWRR_map <-- {f, new_option()}
            remove(f, Functions)
        end if
    end for
    
    // -- Extentension Heuristics -- //

    for f in Functions do
        if f' = propagate(f, to_instrument) then
            to_instrument <-- {f, error_code(f')}
            SWRR_map <-- {f, new_option()}
            remove(Functions, f)
        end if
    end for

    for f in Functions do
        if f' = indirect(f, to_instrument) then
            SWRR_map <-- {f, option(f')}
        end if
    end forkk
```
Are any more Heuristics necessary?
Is the indirect Heuristic actually any good?
> "If a function is called by functions that have been identified as eligible for instrumentation, Talos takes advantage of the fact that by disabling all the callers of the function, the function itself can be disabled by SWRRs. In these cases, Talos does not insert any instrumentation into these functions, but simply updates the SWRR map to indicate that the function in question can be disabled by activating one or more SWRRs.

Research strategies for Java, Jvm, Source code.

From open questions.
> Runtime disabling of functions?
> 
> JVM agents can be used for instrumentation? These are usually used for statistics (research this.)
>
> in-place deployment, should users be able to themselves selectively disable certain functions through editing the configuration file? Could probably be an security issue. Inappropriate for proprietary code which would be obfuscated either way.
>
> Are there any ways other than errors to redirect control flow safely?

Questions project plan
> Which method could be used to answer the question 'Can i discover any vulnerability created by instrumentation with Talos'?
>
> Should this fork extension of Talos be given a name and if so is there anything important to consider?
>
> What risks can be identified and if they become unavoidable how can they be amended.

# Project plan
### Which specific questions will be answered within the scope of the project
1. Will the program described in 'Talos: Neutralizing Vulnerabilities with Security Workarounds for Rapid Response' be extensible to languages with structured error-handling such as Java?

2. Will programs in java instrumented in a similar way be able to cover a similar amount of vulnerabilities.

3. Can i discover any vulnerability created by instrumentation using Talos?
### Which methods will be used to answer these questions
Question 1. can be answered through an experiment. 

Question 2. can be answered in a similar way to the original article. Applicable vulnerabilities should be collected from an database such as nist, data should then be collected on whether these were able to be neutralized using instrumented 'software workarounds for rapid response' and if these workarounds were 'obtrusive'.

Question 3. Not sure. 

### Hur will the results found be evaluated
Results will be evaluated in comparison to the original paper.

### Which relevant literature has been identified
Talos: Neutralizing Vulnerabilities with Security Workarounds for Rapid Responds

### What will be implemented?
A version of Talos extended to be able to instrument Java.
(Should perhaps be given a name?)

### Which working method will be used
This project will use an agile methodology reviewing each sprint every other monday and setting goals based on what was achieved.

### Detailed timeplan 
v.4&emsp;&emsp; Project plan, Forward snowballing analysis, Presentation 'Talos'

v.5&emsp;&emsp; Project plan, Forward snowballing analysis, Presentation 'Talos'

v.6&emsp;&emsp; JVM Research, Talos Source code research, Deadline Project plan

v.7&emsp;&emsp; JVM Research, Talos Source code research, Implementation

v.8&emsp;&emsp; Implementation

v.9&emsp;&emsp; Implementation

v.10&emsp;&ensp; Implementation

v.11&emsp;&ensp; Implementation, Midway seminar: 'Problem statement', 'state of the art', 'Research questions', 'Expected Results', 'Method', 'Possible related issues' <br/>--> _Introduction & Background should be finished here_

v.12&emsp;&ensp; Experimentation

v.13&emsp;&ensp; Experimentation

v.14&emsp;&ensp; Research possible vulnerabilities caused by instrumentation

v.15&emsp;&ensp; Research possible vulnerabilities caused by instrumentation

v.16&emsp;&ensp; Finalize any remaining issues, Write paper

v.17&emsp;&ensp; Write paper

v.18&emsp;&ensp; Write paper

v.19&emsp;&ensp; Write paper

v.20&emsp;&ensp; Write paper

v.21&emsp;&ensp; Write paper, Deadline: **submission of paper**

v.22&emsp;&ensp; Write presentation, present.
### Additional information

### Risks 
 - What happends if neccessary software is inaccessible
 > Talos was found on GitHub and has been forked to [my personal Git](https://github.com/Henrikswoon/Talos/tree/master)
 - What can be done if you don't have enough time at the end of the project
 > Not sure, since the theory part of this thesis is still a little unclear until the implementation has been finalized. Perhaps this could be discussed? 

### Agreements in supervision
The code produced should be under open source license, since TALOS uses GNU GPL this license would probably be suitable.

### Address to the journal
_Currently unavailable, will make accessible asap._

[henriksson-melker.se/masters-thesis/journal](henriksson-melker.se/masters-thesis/journal)

### Supervision
Alexandre Bartel    - Umeå Universitet

Anders Sigfridsson  - Omegapoint

Evelina Malmqvist   - Omegapoint

---
Melker Henriksson
9910180158
