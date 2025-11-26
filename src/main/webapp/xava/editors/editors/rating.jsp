<%@ page import="org.openxava.view.View" %>
<%
    View view = (View) request.getAttribute("xava_view");
    String property = (String) request.getAttribute("xava_property");

    String id = "rating_" + property;
    Object valueObj = view.getValue(property);
    int value = (valueObj == null) ? 0 : (Integer) valueObj;
    boolean readOnly = !view.isEditable(property);
%>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/xava/style/rating.css"/>

<div class="rating-container" id="<%=id%>">
    <% for(int i = 1; i <= 5; i++) { %>
    <span data-value="<%=i%>" class="<%= (i <= value ? "selected" : "") %>">&#9733;</span>
    <% } %>
</div>

<input type="hidden"
       id="<%=id%>_input"
       name="<%=view.getPropertyPrefix() + property%>"
       value="<%=value%>" />

<script>
    document.addEventListener('DOMContentLoaded', function() {

        const container = document.getElementById('<%=id%>');
        const input = document.getElementById('<%=id%>_input');

        if (!<%=readOnly%>) {
            container.querySelectorAll('span').forEach(star => {
                star.addEventListener('click', () => {
                    let v = parseInt(star.dataset.value);
                    input.value = v;

                    container.querySelectorAll('span')
                        .forEach(s => s.classList.toggle('selected', s.dataset.value <= v));
                });
            });
        }
    });
</script>
