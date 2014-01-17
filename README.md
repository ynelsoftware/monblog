monblog 0.6.0
=============

Backend for a Blog in MongoDB

#####Upgrading from 0.5.0 to 0.6.0#####

    db.blogpost.update({},{$set:{visits:0}},{multi:true})