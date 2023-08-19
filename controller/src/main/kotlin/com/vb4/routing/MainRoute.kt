package com.vb4.routing

import com.vb4.plugins.AUTH_SESSION
import com.vb4.routing.destinations.dms.index.destinationsDMsIndexPost
import com.vb4.routing.destinations.dms.show.destinationsDMsShowGet
import com.vb4.routing.destinations.dms.show.destinationsDMsShowPost
import com.vb4.routing.destinations.dms.show.messages.show.destinationsDMsShowMessagesShowGet
import com.vb4.routing.destinations.dms.show.messages.show.destinationsDMsShowMessagesShowPost
import com.vb4.routing.destinations.groups.index.destinationsGroupsIndexPost
import com.vb4.routing.destinations.groups.show.destinationsGroupsShowGet
import com.vb4.routing.destinations.groups.show.destinationsGroupsShowPost
import com.vb4.routing.destinations.groups.show.messages.show.destinationsGroupsShowMessagesShowGet
import com.vb4.routing.destinations.groups.show.messages.show.destinationsGroupsShowMessagesShowPost
import com.vb4.routing.destinations.index.destinationsIndexGet
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
        authenticate(AUTH_SESSION) {
            route("destinations") {
                // DMとGroupを検索する機能
                destinationsIndexGet()

                route("dms") {
                    // 新しいDM先を追加
                    destinationsDMsIndexPost()

                    // 特定のDM先とのメッセージを取得
                    destinationsDMsShowGet()
                    // 特定のDM先宛の新しいメッセージを作成
                    destinationsDMsShowPost()

                    // 特定のDM先との特定のメッセージを取得
                    destinationsDMsShowMessagesShowGet()
                    // 特定のDM先との特定のメッセージへの返信を作成
                    destinationsDMsShowMessagesShowPost()
                }
                route("groups") {
                    // 新しいGroup先を追加
                    destinationsGroupsIndexPost()

                    // 特定のGroupとのメッセージを取得
                    destinationsGroupsShowGet()
                    // 特定のGroup先宛の新しいメッセージを作成
                    destinationsGroupsShowPost()

                    // 特定のGroupとの特定のメッセージへの返信を作成
                    destinationsGroupsShowMessagesShowGet()
                    // 特定のGroup先との特定のメッセージへの返信を作成
                    destinationsGroupsShowMessagesShowPost()
                }
            }
        }
    }
}
