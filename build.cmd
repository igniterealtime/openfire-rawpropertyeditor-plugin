call mvn clean package -Dmaven.test.skip=true

cd target
rename rawpropertyeditor-openfire-plugin-assembly.jar rawpropertyeditor.jar
rd "D:\Openfire\openfire_4_8_1\plugins\rawpropertyeditor" /q /s
del "D:\Openfire\openfire_4_8_1\plugins\rawpropertyeditor.jar" 
del /q "D:\Openfire\openfire_4_8_1\logs\*.*"
copy rawpropertyeditor.jar D:\Openfire\openfire_4_8_1\plugins\rawpropertyeditor.jar

pause