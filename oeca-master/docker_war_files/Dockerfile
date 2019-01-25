FROM jetty

#local
ADD oeca-svc-acl.war          /var/lib/jetty/webapps/oeca-svc-acl.war
ADD oeca-svc-auth.war         /var/lib/jetty/webapps/oeca-svc-auth.war
ADD oeca-svc-ref.war          /var/lib/jetty/webapps/oeca-svc-ref.war
ADD oeca-svc-registration.war /var/lib/jetty/webapps/oeca-svc-registration.war
ADD oeca-gmg-web.war          /var/lib/jetty/webapps/oeca-gmg-web.war


CMD java -jar $JETTY_HOME/start.jar -Dspring.config.dir=".."

EXPOSE 8080