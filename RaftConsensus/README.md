# networks-keyvalueDS
This is our sixth project for our Networks & Distributed Systems course.

This project's goal is to implement a (relatively) simple, distributed, replicated key-value datastore. A key-value datastore is a very simple type of database that supports two API calls from clients: put(key, value) and get(key). The former API allows a client application to store a key-value pair in the database, while the latter API allows a client to retrieve a previously stored value by supplying its key.

High-level approach:

Our approach to this project was to first understand what was provided in the starter code, which we saw that there was an intial very barebones get request sent to a socket. From here, we researched heavily into raft protocol and used the provided raft guideline to construct our general view of how raft works and some possible implementation pointers that would be useful. The Raft algorithm we drafted had the following guidelines: the algorithm elects one of the replicas as a leader, and all other replicas become followers. The leader handles all client requests, while followers and candidates (replicas that are running for election) forward client requests to the leader. The Replica class has three states: FOLLOWER, CANDIDATE, and LEADER. The state transitions are determined by timeouts and messages exchanged between replicas. The handle_message method is the core of the algorithm. It handles the different types of messages that replicas can send to each other, such as heartbeat messages, vote request messages, and client request messages. The run method is the main method that runs the Raft algorithm. It loops indefinitely, waiting for messages from other replicas and handling them accordingly. The runLeader, runCandidate, and runFollower methods are state-specific methods that handle different tasks for leaders, candidates, and followers, respectively. For example, the runLeader method sends heartbeat messages to other replicas periodically to maintain its leadership. By isolating the states and cornerning the responsibilities for each it was much easier to work through the protocol. Keeping track of the log was something we did to ensure everything was being sent and received safely, securely, and completely.


Challenges:
  - Understanding how to begin the protocol (who, when, and how to elect).
  - Figuring out the responsibilities of each state replica and trying to abstract them to optomize code
  - Ensuring everything is being keep track of and maintained/updated.
  - Coordinating election timeouts
  
  
Some features that I particularly liked was how condensed and abstractd our code is. Each state does exactly what it needs to and no more. Additionally, the fact that each run condition is isolated can introduce a lot of scalability if we decide to improve our protocol on the end of a specific state. So, in doing so, it will be much easier to improve our code in the future without derailing our entire functionality.
  
Testing Strategy: We tested our code through running the code and printing out the log of what was sent and receieved.  We tested our code through running the code and printing out the values of what was outputted from the server using './run configs/simple-1.json' and other lines according to the file we wanted to test. Debug print statements were essential. We printed out the values of the states of each replica, their vote counts, term counts, frontier (queue for nodes to be synchronized), and election times. For this reason, the log was a huge help. We ensured consistent results through running the code multiple times, and also through submitting it to the Gradescope autograder. We made some other tests that tested whole functionality of parts of our program, incrementally adding elements to pinpoint problem areas. So, we used some functional testing as well as some integration testing, aside from general debugging/printing for output testing.
