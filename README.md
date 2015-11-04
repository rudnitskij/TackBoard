# TackBoard
Task made for testing purposes<br>
TackBoard - Доска объявлений<br>
The project uses Spring MVC in its work and you have to make some change in configuration file tackboard-servlet.xml for the purpose of proper application working
'src\main\webapp\WEB-INF\tackboard-servlet.xml' is Spring mvc configuration file.
<bean id="projectPath" class="java.lang.String">
        <constructor-arg value="D:\Java projects\Tackboard\"/>
    </bean>
This is what you have to find within xml-file and replace <i>value</i> property with the actual path of the folder to which you have downloaded the whole project from the repository 
(value must end with the backslash)<br>
After that, locate src\main\resources\META-INF\persistence.xml which is in charge for database connection settings.
The application is supposed to be used with MySQL database. First of all it is required to create database schema by executing in SQL console the following request:<br>
<code> create schema '{schemaname}'</code><br>
{Schemaname} stands for the name chosen by you and this very name must be specified in the following line of <b>persistence.xml</b> file:<br>
<code>&#60;property name="hibernate.connection.url" value="jdbc:mysql://localhost:3306/{schemaname}" /></code><br>
Your username and password for MySQL database are supposed to be specified in the following lines of the file:<br>
<code>&#60;property name="hibernate.connection.username" value="{username}" /></code><br>
<code>&#60;property name="hibernate.connection.password" value="{password}" /></code><br>
