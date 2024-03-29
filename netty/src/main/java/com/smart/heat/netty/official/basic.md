# Netty
## question
现在我们使用通用的应用程序或库来彼此通信。例如，我们经常使用HTTP客户端库从web服务器检索信息并通过web服务调用远程过程调用，然而，通用协议或其实现有时不能很好地扩展。这就像我们不使用通用的HTTP服务器来交换巨大的文件、电子邮件和近乎实时的消息，比如财务信息和多人游戏数据。我们需要的是一个高度优化的协议实现，它专门用于一个特殊的用途。例如，您可能希望实现一个HTTP服务器，该服务器针对基于AJAX的聊天应用程序、媒体流或大文件传输进行了优化。你甚至可以设计和实现一个完全适合你需要的新协议，另一个不可避免的情况是你必须处理一个遗留的专有协议，以确保与旧系统的互操作性。在这种情况下，重要的是我们可以多快地实现该协议，同时又不牺牲结果应用程序的稳定性和性能。
## 解决方案
Netty项目旨在为快速开发可维护的高性能和高可扩展性协议服务器和客户端提供异步事件驱动的网络应用程序框架和工具。

换句话说，Netty是一个NIO客户机-服务器框架，它支持快速、轻松地开发网络应用程序，如协议服务器和客户端。它大大简化和简化了网络编程，如TCP和UDP套接字服务器的开发。

“快速和简单”并不意味着最终的应用程序将受到可维护性或性能问题的影响。Netty经过精心设计，从FTP、SMTP、HTTP以及各种二进制和基于文本的遗留协议的实现中吸取了经验。结果，Netty成功地找到了一种方法，在不妥协的情况下实现开发的易用性、性能、稳定性和灵活性。

一些用户可能已经发现了其他网络应用程序框架，它们声称具有同样的优势，您可能想问一下，是什么使Netty与他们有如此大的不同。答案是它所依据的哲学。Netty的设计初衷是让您在API和实现的第一天就获得最舒适的体验。这不是什么有形的东西，但你会意识到，这种哲学将使你的生活更容易，因为你读了这本指南，并与Netty玩。

## 入门
本章通过简单的例子介绍Netty的核心结构，让您快速入门。当你在本章结束时，你就可以在Netty上写一个客户机和一个服务器。

如果你喜欢自上而下的学习方法，你可以从第2章，概述然后回来
## 开始之前
运行本章中示例的最低要求只有两个：Netty和jdk1.6或更高版本。Netty的最新版本可在[下载项目页面](https://netty.io/downloads.html). 要下载正确版本的JDK，请访问您首选的JDK供应商的网站。

当你阅读时，你可能对本章介绍的课程有更多的问题。如果您想了解更多的信息，请参考API参考资料。为了方便起见，本文档中的所有类名都链接到联机API引用。还有，请不要犹豫联系Netty项目社区如果有任何不正确的信息，语法错误或打字错误，请告诉我们，如果你有什么好主意来帮助改进文档。

