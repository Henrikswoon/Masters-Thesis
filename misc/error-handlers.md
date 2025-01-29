# Error Handlers

### Intra-procedural 
<p>Operate completely within a function.</p>
```
//Intra-procedural
function error_handling -> value | error () {

    if (condition){
        return foo(x);
    } else {
        free(resources);
        return error;
    }
}
```
---


### Inter-procedural
<p>Expose the error to the functions caller.</p>
```
//Inter-procedural
//Caller either receives expected value
//Or error, which causes it to release resources
function caller -> value | error () {
    if (val = error_handling(x) == error){
        free(resources);
        return error;
    } else {
        foo(val);
        free(resources);
    }
}

//Function either succeeds and returns the expected value
//Or fails and exposes error to caller
function error_handling -> val | error  (x) {
    if (!is_valid(x)) return error;
    return foo(x);
}
```
---

