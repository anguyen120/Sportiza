$(document).ready(function() {
    // Check if the current URL contains '#'
    if (document.URL.indexOf("#") == -1) {
        // Set the URL to whatever it was plus "#".
        url = document.URL + "#";
        location = "#";

        // Reload the page
        location.reload(true);
    }

   // Render graph
   $("#graph-container").empty();

   document.getElementById('graph-container').setAttribute("style","width:700px");
   document.getElementById('graph-container').setAttribute("style","height:600px");

   var graph = {
     nodes: [],
     edges: []
   };

   graphData["nodes"].forEach (function (node) {
     graph.nodes.push({
       id: node.id,
       label: node.label,
       x: Math.random(),
       y: Math.random(),
       size: node.size * 2,
       color: '#2ee4bb'
     });
   });

   graphData["edges"].forEach (function (edge) {
     graph.edges.push({
       id: edge.id,
       source: edge.source,
       target: edge.target,
       size: 3,
       type: edge.type,
       label: edge.id.split("-")[1],
       color: 'rgba(0, 0, 0, 0.25)'
     });
   });

   // Instantiate sigma:
   s = new sigma({
     graph: graph,
     renderer: {
       container: 'graph-container',
       type: 'canvas'
     },
     settings: {
       maxEdgeSize: 5
     }
   });

   // Initialize the dragNodes plugin for graph:
   var dragListener = sigma.plugins.dragNodes(s, s.renderers[0]);

   dragListener.bind('startdrag', function(event) {
   });
   dragListener.bind('drag', function(event) {
   });
   dragListener.bind('drop', function(event) {
   });
   dragListener.bind('dragend', function(event) {
   });
});
