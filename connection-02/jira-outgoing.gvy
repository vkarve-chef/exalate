for ( component in issue.components ) {
    if ( component.name != "Prod-B" ) {
        return // sync back only if component matches repo
    }
}
replica.key            = issue.key
// replica.type           = issue.type
// replica.assignee       = issue.assignee
// replica.reporter       = issue.reporter
replica.summary        = issue.summary
replica.description    = issue.description
// replica.labels         = issue.labels
replica.comments       = issue.comments.findAll { it -> it.body.toLowerCase().startsWith("[public]") }
replica.resolution     = issue.resolution
replica.status         = issue.status
// replica.parentId       = issue.parentId
// replica.priority       = issue.priority
replica.attachments    = issue.attachments
replica.project        = issue.project

//Comment these lines out if you are interested in sending the full list of versions and components of the source project. 
replica.project.versions = []
replica.project.components = []

/*
Custom Fields

replica.customFields."CF Name" = issue.customFields."CF Name"
*/