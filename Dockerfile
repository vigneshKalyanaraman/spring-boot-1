FROM tomcat:9-jre8-alpine
  
MAINTAINER pradeep.k@optisolbusiness.com

WORKDIR /usr/local/tomcat

COPY target/travelProLogin.war /usr/local/tomcat/webapps/

# Start the tomcat (and leave it hanging)

EXPOSE 9002

CMD ["catalina.sh", "run"]