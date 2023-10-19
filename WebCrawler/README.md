# networks-WebCrawler
This is our fifth project for our Networks & Distributed Systems course.

This project's goal is to implement a web crawler that gathers data from a fake social networking website. There are several educational goals of this project:

Get exposure to the HTTP protocol, which underlies a large (and growing) number of applications and services today.
To see how web pages are structured using HTML.
To get experience implementing a client for a well-specified network protocol.
To understand how web crawlers work, and how they are used to implement popular web services today

High-level approach:

Our approach to this project was to first understand what was provided in the starter code, which we saw that there was an intial very barebones get request sent to a socket. We decided that logging in would be the first thing that we should do just to get started. We also found that cookie tracking was essential for the crawler at this point, and identified CSRF and session ID tokens. Get request was the main issue in handling this, and figuring out how to parse the received HTML response. 

Once get was configured, we had to actually log in, and so post was created. At this point, it was much smoother sailing as all we had left was some event handling and secret flag identification. Our approach to the frontier was quite simple, ensuring that we only visited new links, and revisisted links that had to be visisted again. After some cookie handling/maintenance, our crawler was functioning. 

We noticed the length of time the crawler was taking to run, so we implemented keep-alive and added functionality for GZIP compression/decompression. By using the threading library, we were able to create 5 threads at a time, each popping an item from the Deque of URLs to be visited,and crawl that URL. The target function for each thread was the process_response function, which sends the HTTP post or get request and processes the response, and of course updates the frontier Deque and the Set of visited URLs. 5 threads were created at a time to avoid overloading the server (as instructed in description). After creating the threads, we join them to ensure all 5 (at a maximum)have finished. To ensure that the program is thread safe, we created a class field thread lock, and would aquire it whenever we either update the Deque or Set, or when we perform a socket I/O. We noticed significant performance benefits (~93% improvement) as a result of making our crawler concurrent and compression/keep-alive friendly.

Challenges:
  - Understanding how to form a request correctly using the needed HTML components
  - Understanding the login process and how exactly the requests should be received/sent
  - Figuring out how to parse the responses to our requests
  - Actually logging in using get and configuring the parsed elements from the post request
  - Waiting for the crawler to run for several minutes everytime just to test functionality was frustrating
  
Testing Strategy: We tested our code through running the code and printing out the values of what was outputted from the server. Since there was not config test file for this assignment, debug print statements were essential. We printed out the values of the frontier, secret flags list, HTML responses, and status codes to keep track of how our crawler was functioning. We ensured consistent results through running the code multiple times, and also through submitting it to the Gradescope autograder. We made some other tests that tested whole functionality of parts of our program, incrementally adding elements to pinpoint problem areas. So, we used some functional testing as well as some integration testing, aside from general debugging/printing for output testing.
