package com.todos

import zio._

package object repository {
  type TodoRepository = Has[TodoRepository.Service]
}
