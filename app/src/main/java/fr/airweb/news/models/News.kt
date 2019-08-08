package fr.airweb.news.models


import io.realm.Realm
import io.realm.RealmObject

//"nid": 1,
//"type": "news",
//"date": "2018-05-29",
//"title": "Les Bleus s'imposent avec Kimpembe et Mbappé",
//"picture": "https://psgimages.stadion.io/media/2554/068_aa_29052018_754647.jpg?anchor=center&mode=crop&width=450&height=338&quality=60",
//"content": "L'Équipe de France des Parisiens Alphonse Areola, Presnel Kimpembe et Kylian Mbappé s'est imposée 2-0 face à la République d'Irlande, ce lundi 28 mai au Stade de France, en match amical.",
//"dateformated": "29/05/2018"


open class News : RealmObject {

    constructor(
        someStr1: String,
        type: String,
        date: String,
        title: String,
        picture: String,
        content: String,
        dateformated: String
    ) {
        this.nid = someStr1
        this.type = type
        this.date = date
        this.title = title
        this.picture = picture
        this.content = content
        this.dateformated = dateformated
    }

    constructor()

    var nid: String = ""
        get() = field
        set(value) { field = value }


    var type: String = ""
        get() = field
        set(value) { field = value }


    var date: String = ""
        get() = field
        set(value) { field = value }


    var title: String = ""
        get() = field
        set(value) { field = value }


    var picture: String = ""
        get() = field
        set(value) { field = value }


    var content: String = ""
        get() = field
        set(value) { field = value }


    var dateformated: String = ""
        get() = field
        set(value) { field = value }


}