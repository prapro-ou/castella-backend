package com.vb4.routing

import com.vb4.plugins.auth.JWT_AUTH
import com.vb4.routing.destinations.index.destinationsIndexGet
import com.vb4.routing.dms.index.dMsIndexPost
import com.vb4.routing.dms.show.dmsShowGet
import com.vb4.routing.dms.show.dmsShowPost
import com.vb4.routing.dms.show.messages.show.dMsShowMessagesShowPost
import com.vb4.routing.groups.show.groupsShowGet
import com.vb4.routing.dms.show.messages.show.dmsShowMessagesShowGet
import com.vb4.routing.groups.messages.show.groupsMessagesShowGet
import com.vb4.routing.login.index.loginIndexPost
import com.vb4.routing.register.index.registerIndexPost
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.mainRoute() {
    routing {
        get { call.respond("Success") }
        route("login") {
            loginIndexPost()
        }
        route("register") {
            registerIndexPost()
        }
        authenticate(JWT_AUTH) {
            route("destinations") {
                // DMとGroupを検索する機能
                destinationsIndexGet()
            }
            route("dms") {
                // 新しいDM先を追加
                dMsIndexPost()

                // 特定のDM先とのメッセージを取得
                dmsShowGet()
                // 特定のDM先宛の新しいメッセージを作成
                dmsShowPost()

                // 特定のDM先との特定のメッセージを取得
                dmsShowMessagesShowGet()
                // 特定のDM先との特定のメッセージへの返信を作成
                dMsShowMessagesShowPost()
            }
            route("groups") {
                // 特定のGroupとのメッセージを取得
                groupsShowGet()

                // 特定のGroupとの特定のメッセージへの返信を作成
                groupsMessagesShowGet()
            }
        }
    }
}
