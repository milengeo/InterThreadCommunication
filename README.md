# InterThreadCommunication
Android example of lock-free inter-thread communication.

# The problem
There are several ways for communicating between threads in Android platform.
Most of them use synchronization locks, which introduce performance penalties.
In many cases, the ANRs (Application Not Responding) causes are related to locking waits.

# The solution
This sample shows how to communicate between two threads without using synchronization locks.
That is done by utilizing the Android specific classes Handler and Looper.
The sending thread creates a POJO object with desired data and posts a Runnable with it to the target thread Handler queue. 
And the response is sent by the same way in the opposite direction.

# The result
We can achieve two-way inter-thread communication without the performance penalty of the synchronization locks.