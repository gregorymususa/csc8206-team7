csc8206-team7
CSC8206 Team 7 Group Project


#Initial Setup
On your PC navigate, to where you save your schoolwork, and create the directory:
mkdir "csc8206-team7"

This is where the contents of the repository will be.
We are using Eclipse MARS, Java 8

In the folder you just created, type the following:
git init
git remote add origin https://github.com/gregorymususa/csc8206-team7
git config --global user.name "yourgithubusername"
git config --global user.email your.username@emailprovider.com


#Daily use
// PULL changes FROM GitHub

git pull origin master

// PUSH changes TO GitHub (-f force) (./* is where you put the files you want to add)

git commit -a -m "Commit notes"

* "-a" Commits modified and deleted files only
* "-f" Forces "github ignored" files to be committed (may or may not come in handy)
* "-m" Commit notes

git push -f <remote-name> <branch-name>
git push -f origin master


# Roll back
*ASK TEAM BEFORE YOU DO THIS — EVERYONE HAS TO AGREE*

git reset --hard <old-commit-id>


# Setting up Eclipse
Open Eclipse MARS, and use any folder of your choosing as workspace (except the folder you synced with git)

Import "Existing Projects into Workspace" — select the git folder

Eclipse should link to the project