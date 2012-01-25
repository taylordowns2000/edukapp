echo Stopping Tomcat
call c:\apache-tomcat-7.0.23\bin\shutdown.bat
echo Running ant clean-deploy
ant clean-deploy
echo Start Tomcat
call c:\apache-tomcat-7.0.23\bin\startup.bat