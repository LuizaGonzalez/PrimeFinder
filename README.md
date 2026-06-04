# PrimeFinder — ARSW Lab #2 Part I
 
**Escuela Colombiana de Ingeniería – Software Architectures**  
Concurrent programming lab: wait/notify, monitors and synchronization without busy-wait.
 
---
 
## Requirements
 
- **JDK 21** (Temurin recommended)
- **Maven 3.9+**
- OS: Windows, macOS or Linux
---
 
## How to run
 
```bash
mvn clean verify
mvn exec:java
```
 
---
 
## Description
 
The program searches for prime numbers in the range `[2, 30.000.000)` using **3 worker threads** in parallel. Every **5 seconds** the program pauses, shows how many primes have been found so far and waits for the user to press **ENTER** to continue.
 
---
 
## Architecture
 
```
edu.eci.arsw.primefinder
├── Main.java               # Entry point
├── Control.java            # Controller thread (pause/resume)
├── PauseControl.java       # Shared monitor (wait/notify)
└── PrimeFinderThread.java  # Worker threads (search for primes)
```
 
---
 
## Lab Report — Part I
 
### Objectives
- Practice thread synchronization in Java using synchronized, wait() and notifyAll() on a shared monitor.
- Eliminate busy-wait by replacing it with real thread suspension.
- Understand the monitor model in Java and how to coordinate multiple worker threads from a controller thread.
- Guarantee consistency when reading shared state at the moment statistics are displayed.
---
 
### 1. Synchronization design
 
#### Monitor object
The _PauseControl_ class was created as a shared monitor between all threads. This object is the only lock used for synchronization. All relevant methods are marked as _synchronized_, which guarantees that only one thread can execute them at a time.
 
---
 
#### Guard condition
The _boolean paused_ flag is the guard condition. When set to _true_, workers must suspend themselves. When set to _false_, workers can run freely.
 
---
 
### 2. How busy-wait is avoided
 
Busy-wait occurs when a thread repeatedly checks a condition without yielding the processor, consuming CPU unnecessarily. In this implementation it is completely avoided by using _wait()_ inside _checkPause()_. When a thread enters _wait()_ three things happen: it releases the monitor lock so other threads can enter, it suspends completely, and it only wakes up when someone calls _notifyAll()_.
 
---
 
### 3. How lost-wakeups are avoided
 
A lost-wakeup occurs when _notify()_ is called before the thread enters _wait()_, leaving the thread asleep forever. It is avoided by using _while_ instead of _if_ to evaluate the guard condition. The _while_ guarantees that upon waking up, the thread re-evaluates _paused_ before continuing. If the condition still holds due to a spurious wakeup, the thread goes back to sleep. If it no longer holds, it continues its work.
 
---
 
### Conclusions
- Using wait() inside a synchronized block allows a thread to suspend completely without consuming CPU, unlike busy-wait which degrades system performance.
- Using while instead of if to evaluate the guard condition is essential to avoid both lost-wakeups and spurious wakeups, making synchronization more robust.
- notifyAll() is necessary when multiple threads wait on the same monitor, since notify() would only wake one and the rest would remain blocked indefinitely.
- The monitor object (PauseControl) centralizes all synchronization logic, keeping the code clean with well-defined responsibilities.
- It is important to verify whether worker threads are still alive before attempting to pause them, to avoid calling synchronization operations on already-finished threads.
---
 
## Credits
 
Lab based on the **wait-notify-excercise** by ARSW-ECI.  
**Original base by Ing. Javier Toquica.**
