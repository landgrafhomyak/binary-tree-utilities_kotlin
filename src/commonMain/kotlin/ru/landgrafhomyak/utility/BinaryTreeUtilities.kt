@file:JvmName("BinaryTreeUtilitiesKt")

package ru.landgrafhomyak.utility

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic

@Suppress("FunctionName")
object BinaryTreeUtilities {
    object Swap {
        @JvmStatic
        @OptIn(ExperimentalContracts::class)
        @PublishedApi
        internal inline fun <NODE : Any> NODE?.setParentNullable(
            parent: NODE?,
            setter: (node: NODE, newParent: NODE?) -> Unit
        ) {
            contract {
                callsInPlace(setter, InvocationKind.AT_MOST_ONCE)
            }
            if (this@setParentNullable != null)
                setter(this@setParentNullable, parent)
        }

        @JvmStatic
        @OptIn(ExperimentalContracts::class)
        inline fun <NODE : Any> _swap_ParentChild(
            parent: NODE, child: NODE,
            grandparent: NODE?,
            setGrandparent2Parent: (node: NODE) -> Unit,
            setParent: (node: NODE, newParent: NODE?) -> Unit,
            getForwardChild: (node: NODE) -> NODE?,
            setForwardChild: (parent: NODE, child: NODE?) -> Unit,
            getOppositeChild: (node: NODE) -> NODE?,
            setOppositeChild: (parent: NODE, child: NODE?) -> Unit
        ) {
            contract {
                callsInPlace(setGrandparent2Parent, InvocationKind.EXACTLY_ONCE)
                callsInPlace(setParent, InvocationKind.AT_LEAST_ONCE)
                callsInPlace(getForwardChild, InvocationKind.AT_LEAST_ONCE)
                callsInPlace(setForwardChild, InvocationKind.AT_LEAST_ONCE)
                callsInPlace(getOppositeChild, InvocationKind.AT_LEAST_ONCE)
                callsInPlace(setOppositeChild, InvocationKind.AT_LEAST_ONCE)
            }

            setParent(child, grandparent)
            setGrandparent2Parent(child)
            setForwardChild(parent, getForwardChild(child))

            val oppositeChild = getOppositeChild(parent)
            setOppositeChild(parent, getOppositeChild(child))
            setOppositeChild(child, oppositeChild)

            getOppositeChild(child).setParentNullable(child, setParent)
            getOppositeChild(parent).setParentNullable(parent, setParent)
            getForwardChild(child).setParentNullable(parent, setParent)
            setForwardChild(child, parent)
            setParent(parent, child)
        }

        @JvmStatic
        @OptIn(ExperimentalContracts::class)
        inline fun <NODE : Any> _swap_Random(
            node1: NODE, node2: NODE,
            node1parent: NODE?, node2parent: NODE?,
            setParent2Node1: (node: NODE) -> Unit,
            setParent2Node2: (node: NODE) -> Unit,
            setParent: (node: NODE, newParent: NODE?) -> Unit,
            getLeftChild: (node: NODE) -> NODE?,
            setLeftChild: (parent: NODE, child: NODE?) -> Unit,
            getRightChild: (node: NODE) -> NODE?,
            setRightChild: (parent: NODE, child: NODE?) -> Unit
        ) {
            contract {
                callsInPlace(setParent2Node1, InvocationKind.EXACTLY_ONCE)
                callsInPlace(setParent2Node2, InvocationKind.EXACTLY_ONCE)
                callsInPlace(setParent, InvocationKind.AT_LEAST_ONCE)
                callsInPlace(getLeftChild, InvocationKind.AT_LEAST_ONCE)
                callsInPlace(setLeftChild, InvocationKind.AT_LEAST_ONCE)
                callsInPlace(getRightChild, InvocationKind.AT_LEAST_ONCE)
                callsInPlace(setRightChild, InvocationKind.AT_LEAST_ONCE)
            }

            setParent2Node1(node2)
            setParent2Node2(node1)
            setParent(node1, node2parent)
            setParent(node2, node1parent)

            val leftChild1 = getLeftChild(node1)
            val leftChild2 = getLeftChild(node2)
            setLeftChild(node1, leftChild2)
            setLeftChild(node2, leftChild1)
            leftChild1.setParentNullable(node2, setParent)
            leftChild2.setParentNullable(node1, setParent)


            val rightChild1 = getRightChild(node1)
            val rightChild2 = getRightChild(node2)
            setRightChild(node1, rightChild2)
            setRightChild(node2, rightChild1)
            rightChild1.setParentNullable(node2, setParent)
            rightChild2.setParentNullable(node1, setParent)
        }
    }
}