package ru.landgrafhomyak.utility.collections

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.jvm.JvmStatic

@Suppress("FunctionName")
object BinaryTreeUtilities {
    /**
     * Contains builders for swap operations in linked binary tree structures.
     *
     * @see BinaryTreeUtilities.Swap.swapDistant
     * @see BinaryTreeUtilities.Swap.swapNeighbours
     */
    object Swap {
        @JvmStatic
        @OptIn(ExperimentalContracts::class)
        @PublishedApi
        internal inline fun <NODE : Any> NODE?._setParentNullable(
            parent: NODE?,
            setter: (node: NODE, newParent: NODE?) -> Unit
        ) {
            contract {
                callsInPlace(setter, InvocationKind.AT_MOST_ONCE)
            }
            if (this@_setParentNullable != null)
                setter(this@_setParentNullable, parent)
        }

        /**
         * Swap variant when [child] is left or right child of [parent].
         *
         * @param grandparent parent of [parent].
         * @param setGrandparent2Parent function for setting new child of [grandparent]. Will be called exactly once,
         * so determination what child [parent] is can be done inside it.
         *
         * @param setParent operator for setting parent of specified node.
         * @param getForwardChild operator for getting child of a same direction as [parent]->[child].
         * @param setForwardChild operator for setting child of a same direction as [parent]->[child].
         * @param getOppositeChild operator for getting child from an opposite direction to [parent]->[child].
         * @param setOppositeChild operator for setting child from an opposite direction to [parent]->[child].
         */
        @JvmStatic
        @OptIn(ExperimentalContracts::class)
        inline fun <NODE : Any> swapNeighbours(
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

            getOppositeChild(child)._setParentNullable(child, setParent)
            getOppositeChild(parent)._setParentNullable(parent, setParent)
            getForwardChild(child)._setParentNullable(parent, setParent)
            setForwardChild(child, parent)
            setParent(parent, child)
        }

        /**
         * Swap variant when [node1] is ***NOT*** left or right child of [node2] and vice versa.
         *
         * This function is symmetric, so 'left' and 'right' operators can be swapped.
         *
         * @param node1parent parent of [node1].
         * @param node2parent parent of [node2].
         * @param setParent2Node1 function for setting new child of [node1parent]. Will be called exactly once,
         * so determination what child [node1parent] is can be done inside it.
         * @param setParent2Node2 function for setting new child of [node2parent]. Will be called exactly once,
         * so determination what child [node2parent] is can be done inside it.
         *
         * @param setParent operator for setting parent of specified node.
         * @param getLeftChild operator for getting left child of specified node.
         * @param setLeftChild operator for setting left child of specified node.
         * @param getRightChild operator for getting right child of specified node.
         * @param setRightChild operator for setting right child of specified node.
         */
        @JvmStatic
        @OptIn(ExperimentalContracts::class)
        inline fun <NODE : Any> swapDistant(
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
            leftChild1._setParentNullable(node2, setParent)
            leftChild2._setParentNullable(node1, setParent)


            val rightChild1 = getRightChild(node1)
            val rightChild2 = getRightChild(node2)
            setRightChild(node1, rightChild2)
            setRightChild(node2, rightChild1)
            rightChild1._setParentNullable(node2, setParent)
            rightChild2._setParentNullable(node1, setParent)
        }
    }
}