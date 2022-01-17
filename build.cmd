call mvn clean package -Dmaven.test.skip=true

cd target
rename rawpropertyeditor-openfire-plugin-assembly.jar rawpropertyeditor.jar
rd "C:\openfire_4_6_2\plugins\rawpropertyeditor" /q /s
del "C:\openfire_4_6_2\plugins\rawpropertyeditor.jar" 
del /q "C:\openfire_4_6_2\logs\*.*"
copy rawpropertyeditor.jar C:\openfire_4_6_2\plugins\rawpropertyeditor.jar


pause