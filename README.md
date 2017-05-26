[![Build Status](https://travis-ci.org/BankingBoys/amos-ss17-proj7.svg?branch=master)](https://travis-ci.org/BankingBoys/amos-ss17-proj7)

# amos-ss17-proj7
Virtual Ledger

# Installation general

You need an environment variable called VIRTUAL_LEDGER_DB_PASSWORD that contains the password for the database.
Restarting the computer/server may be required after setting it!


## Install workspace for eclipse

Open terminal in the rootproject and type

	gradle eclipse

Go to eclipse

1. import
2. existing projects into workspace
3. select root-directory of this project as root directory
4. select "Search for nested projects"
5. select all projects
6. click "Finish"

## Use Jetty-Server

Open Terminal in server-workspace.

Run the command

	gradle jettyRunWar

you build the server-war-file and run it on an embedded jetty server. Changes on the Java-files automatically triggers the server reload (typically < 1 sec).

If you only want to run the server (no war-file build) use:

	gradle jettyRun


Test url: http://localhost:8080/virtual-ledger-server/TestServlet
