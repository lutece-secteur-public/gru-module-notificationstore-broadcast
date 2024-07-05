<jsp:useBean id="managesubscriptionsSubscription" scope="session" class="fr.paris.lutece.plugins.notificationstore.modules.broadcast.web.SubscriptionJspBean" />
<% String strContent = managesubscriptionsSubscription.processController ( request , response ); %>

<%@ page errorPage="../../../../ErrorPage.jsp" %>
<jsp:include page="../../../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../../../AdminFooter.jsp" %>
