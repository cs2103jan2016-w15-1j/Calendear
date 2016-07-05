#CalenDear
##Users' Guide
###1 - Welcome

We are often overwhelmed by different sets of tasks every single day and we often have to spend a huge amount of time organizing and remembering what to do. With CalenDear, you no longer have to be afraid of such problems anymore!

CalenDear is a desktop based task manager that aims to organize your overwhelming number of tasks. With its smart key commands, you can achieve greater efficiency than you would be able to in other task management tools. 

The tasks stored in CalenDear will also be seamlessly recorded in the cloud and available to all your other CalenDear applications connected to Google Calendar.
 
Let us get started!

###2 - Getting Started

Starting up CalenDear is simple. Just follow these steps:

1.	Before we launch CalenDear, let us first get your computer set up with the right ingredients. Head over to http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html and get download JRE8 for your respective operating system.

2.	You can then download CalenDear.exe from the latest release at https://github.com/cs2103jan2016-w15-1j/Calendear/releases

3.	You’re ready to start! Launch CalenDear by double clicking the icon shown on the right. 

 
 
###3 - Features

####3.1 - Adding Tasks

You can add three types of tasks to your CalenDear: 

1.	Task with deadline
2.	Task without deadline (floating task)
3.	Task with a duration of time.

Here’s how you do that.

#####3.1.1 - Adding Deadline Tasks

Most of your tasks have deadlines. For instance, your superior might require you to complete a report by a certain date and time. You can add it into CalenDear simply by entering:

``` add complete report by 18/04/16 14:00 ```

#####3.1.2 - Adding Floating Tasks

Sometimes there are things that you need to do, but without a specific deadline in mind. For example, you might want to complete reading The Lord of the Rings series. Adding such a task is simple, you simply drop the date and time.

``` add read The Lord of the Rings```


#####3.1.3 - Adding Task with a fixed duration

To better plan your schedule, you are able to specify how long each task is going to take you. CalenDear allows you to add such an event like:

``` add meeting with client from 04/10/16 17:00 to 04/10/16 18:00```

After adding any one of those tasks above, CalenDear will display to you the task you have just added like this:
 
#####3.2 - Displaying Tasks
Now that you have added some tasks, let us now take a look at the tasks that you have left. To do so, simply type:

``` display ```

CalenDear will show you the list of tasks you need to accomplish in chronological order.

 



####3.3 - Deleting Tasks
There might be times when your colleague informs you that she has completed the report for you. You can delete the task from CalenDear in two simple steps:

1. Retrieve the identification number (id) of the task by typing: 

``` display ```

	
2. Since “complete report” is id 1, remove the task from CalenDear by typing:

``` delete 1 ```


####3.4 - Updating Tasks
Sometimes the tasks that you added might change or you might want to add additional information to the same task. There is an array of update functionalities that CalenDear provides.

Like how you would delete a task, you need to first find the task id. Then you can update the different fields in the format like this:

``` update <index> <field 1> <New Content 1> <field 2> <New Content 2> ```

| <field> |	<New Content> |
|--------|----------------|
| name	| New Name |
| from	| New Start Time |
| to	| New End Time |
| by	| New Due Time |
| tag	| New Tag |
| at	| New Location |
| important	| - |
| float	| - |
| done	| - |
The table on the right shows you the different fields you can update. For example, after retrieving the id of “read lord of the rings” as 3, you can change the name by typing:

``` update 3 name read lord of the rings part 1 ```

 
####3.5 - Mark Completed Tasks
Finally, you have read the first series of Lord of the Rings. What an accomplishment! You would now like to mark it as completed in CalenDear. After retrieving the id - 3 using the display command, you can mark task 3 as done like this:

``` done 3 ```

####3.6 - Tagging Tasks
Task tagging is a powerful feature that you can use to group various tasks. It can also greatly simplify searching for these groups of events that will be explained later. To add a tag to a task, follow these simple steps:

1.	Retrieve the id using:

``` display ```

2.	If you want to tag the task “attend science lecture” with id 4 with “school”, enter the input:

``` tag 4 school ```

####3.7 - Undo
There might be times when you accidentally deleted the tasks that you need to do. No worries. In CalenDear, you can always revert your actions by entering:

``` undo ```

####3.8 - Redo
After some thought you might think that the task that is deleted might not be worth doing anyways. You can reverse the undo with this command:

``` redo ```

####3.9 - Search for Task
There are times when CalenDear has too many tasks and you want to get the details of the task quickly. CalenDear allows you to do so in three different ways.

#####3.9.1 - Search by Name

You could have forgotten when you need to attend science lecture. Instead of browsing through the entire display output, simply type in:

``` search name attend science lecture ```

The relevant output will be shown like that:

 





#####3.9.2 - Search by Date

You might want to know what tasks you have to complete by tomorrow. You simply have to ask CalenDear:

``` search by tomorrow ```


	
#####3.9.3 - Search by Tag

There are days when you’re in the mood to clear all the tasks related to writing. Considering that you have religiously tagged them. You can get the list of all of such tasks using:

``` search tag writing  ```
####3.10 - Google Calendar Integration
One of the most enabling features of CalenDear is Google Calendar Integration.

#####3.10.1 - Login to Google

After adding all the tasks, you can refer to them by loading them to the cloud. You just have to login to google through our application like this:

``` linkGoogle ```

	
Once you are securely authenticated with google, you’re done! All the tasks you have added will be added to your Google Calendar.

#####3.10.2 - Downloading Cloud Tasks

There are times when you have added tasks to Google Calendar directly. In order to download these tasks to CalenDear, simply type in:
``` syncGoogle ```

And your tasks will be downloaded to CalenDear!

####3.11 - Save
Calendear also allows you to save your current data in another location. To do so, you can simply type: 

``` save <New File Location> ```

####3.12 - Exit Application
To wrap up your day’s work, simply type exit and CalenDear will close itself, automatically remembering all the tasks. 

###4 - Cheat Sheet
Here’s the summary for all of CalenDear’s commands! Don’t worry about forgetting them while using CalenDear as you can view the command summary by typing: 

``` help ```

| Command	| Descriptions |
|---------|--------------|
|add <task Description> by <Due Date>	| Add task with due date  |
| add <Event Description> from< Start Time> to<End Time>	| Add event with fixed duration |
| add <Task Description> |	Add floating task |
| display |	Display all tasks |
| display important	| Display tasks marked as important |
| delete <Id>	| Delete task with id <id> |
|update <Id> <Field> <Content>	|Update field <field> of task with id <id> with new content <content>|
|done <Id>	|Mark task with id <id> as completed|
|tag <Id> <Tag>	|Tag task with id <id> with tag <tag>|
|undo	|Undo the previous command|
|redo	|Redo the previous undo command|
|search <Task Description>	|Search for task with <Task Description> appearing in the task description|
|search <Due Date>	|Search for task with due date |
|search <Tag>	|Search for task with tag <Tag>|
|linkGoogle	|Loads tasks in CalenDear to Google Calendar|
|syncGoogle	|Syncs tasks in CalenDear and Google Calendar|
|save <Path to File>	|Saves the database in a new location.|
|exit	|Exit CalenDear|
